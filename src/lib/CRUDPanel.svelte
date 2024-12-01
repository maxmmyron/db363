<script
  lang="ts"
  generics="T extends {id: Map<string, string>, [k: string]: any}"
>
  import { formatAPIObject } from "$lib";

  let {
    requestSent,
    prim,
    endpoint,
  }: { requestSent: () => void; prim: T; endpoint: string } = $props();

  if (endpoint.at(endpoint.length - 1) == "/") {
    throw new Error("endpoint param cannot end in '/'.");
  }

  let create = async (o: Exclude<T, "id">) => {
    let res = await fetch(formatAPIObject(`${endpoint}/create`, o), {
      method: "POST",
    });
    let t: T["id"] | Exclude<T, "id"> = await res.json();
    console.log(t);
  };

  let read = async (id: Partial<T["id"]>) => {
    // format endpoint to point to entry (id is partial, so for those tables whose keys are composite, partial keys will point to partial GET endpoints)
    let e = endpoint;
    for (const [_, val] of Object.entries(id)) e += `/${val}`;

    let res = await fetch(e, {
      method: "GET",
    });
    let t: T["id"] | Exclude<T, "id"> = await res.json();
    console.log(t);
  };

  let update = async (o: T) => {
    // format endpoint to point to individual entry
    let e = endpoint;
    for (const [_, val] of Object.entries(o.id)) e += `/${val}`;

    // remove "id" from o, and send rest as search params
    let res = await fetch(
      formatAPIObject(
        e,
        Object.entries(o).filter(([k, _]) => k != "id")
      ),
      {
        method: "PUT",
      }
    );
    let t: T["id"] | Exclude<T, "id"> = await res.json();
    console.log(t);
  };

  let del = async (id: T["id"]) => {
    // format endpoint to point to individual entry
    let e = endpoint;
    for (const [_, val] of Object.entries(id)) e += `/${val}`;

    let res = await fetch(e, {
      method: "DELETE",
    });
    let t: T["id"] | Exclude<T, "id"> = await res.json();
    console.log(t);
  };
</script>

{#snippet idInputs(o: T)}
  {#each o["id"].entries() as [k, v]}
    <label for={k}>{k}</label>
    <input name={k} type="text" class="border" />
  {/each}
{/snippet}

{#snippet nonIdInputs(o: T)}
  {#each Object.entries(o) as [k, v]}
    <!-- Skip if ID -->
    {#if k != "id"}
      <label for={k}>{k}</label>
      <input name={k} type="text" class="border" />
    {/if}
  {/each}
{/snippet}

<div class="w-1/2 flex-grow grid border grid-cols-2">
  <!-- CREATE -->
  <section class="mb-2 border-b col-span-2 grid grid-cols-subgrid">
    <h1 class="text-center col-span-2">Create</h1>
    <form
      class="grid grid-cols-subgrid col-span-2"
      onsubmit={(e) => {
        e.preventDefault();
        let formData = new FormData(e.currentTarget);

        let o: { [key: string]: string } = {};
        formData.forEach((v, k) => {
          if (k === "id") return;
          o[k.toString()] = v.toString();
        });

        create(o as Exclude<T, "id">).then(() => requestSent());
      }}
    >
      {@render nonIdInputs(prim)}
      <button type="submit" class="border px-2 py-0.5 col-start-2"
        >Submit</button
      >
    </form>
  </section>
  <section class="mb-2 border-b col-span-2 grid grid-cols-subgrid">
    <h1 class="text-center col-span-2">Read</h1>
    <form
      class="grid grid-cols-subgrid col-span-2"
      onsubmit={(e) => {
        e.preventDefault();
        let formData = new FormData(e.currentTarget);

        let id = new Map<String, string>();
        formData.forEach((v, k) => id.set(k.toString(), v.toString()));

        read(id as T["id"]).then(() => requestSent());
      }}
    >
      {@render idInputs(prim)}
      <button type="submit" class="border px-2 py-0.5 col-start-2"
        >Submit</button
      >
    </form>
  </section>
  <section class="mb-2 border-b col-span-2 grid grid-cols-subgrid">
    <h1 class="text-center col-span-2">Update</h1>
    <form
      class="grid grid-cols-subgrid col-span-2"
      onsubmit={(e) => {
        e.preventDefault();
        let formData = new FormData(e.currentTarget);

        let o: { [key: string]: string } = {};
        formData.forEach((v, k) => {
          if (k === "id") return;
          o[k.toString()] = v.toString();
        });

        update(o as T).then(() => requestSent());
      }}
    >
      {@render idInputs(prim)}
      {@render nonIdInputs(prim)}
      <button type="submit" class="border px-2 py-0.5 col-start-2"
        >Submit</button
      >
    </form>
  </section>
  <section class="mb-2 border-b col-span-2 grid grid-cols-subgrid">
    <h1 class="text-center col-span-2">Delete</h1>
    <form
      class="grid grid-cols-subgrid col-span-2"
      onsubmit={(e) => {
        e.preventDefault();
        let formData = new FormData(e.currentTarget);
        let id = new Map<String, string>();
        formData.forEach((v, k) => id.set(k.toString(), v.toString()));

        del(id as T["id"]).then(() => requestSent());
      }}
    >
      {@render idInputs(prim)}
      <button type="submit" class="border px-2 py-0.5 col-start-2"
        >Submit</button
      >
    </form>
  </section>
</div>
