<script
  lang="ts"
  generics="T extends {id: Map<string, string>, [k: string]: any}"
>
  let {
    requestSent,
    prim,
    endpoint,
  }: { requestSent: () => void; prim: T; endpoint: string } = $props();

  if (endpoint.at(endpoint.length - 1) == "/") {
    throw new Error("endpoint param cannot end in '/'.");
  }

  const formatIDEndpoint = (id: Partial<T["id"]>) => {
    let e = endpoint;
    if (id.size === 0) return e;

    let entries = Object.entries(id);
    if (entries.length === 1 && prim.id.size === 1) e += `/${entries[0][1]}`;
    else {
      for (const [key, val] of entries) e += `?${key}=${val}&`;
      // cut last '&' off
      e.substring(0, e.length - 1);
    }

    return e;
  };

  let create = async (o: Exclude<T, "id">) => {
    let res = await fetch(`${endpoint}/create`, {
      method: "POST",
      body: JSON.stringify(o),
    });
    let t: T["id"] | Exclude<T, "id"> = await res.json();
    console.log(t);
  };

  let read = async (id: Partial<T["id"]>) => {
    let res = await fetch(formatIDEndpoint(id), {
      method: "GET",
    });
    let t: T["id"] | Exclude<T, "id"> = await res.json();
    console.log(t);
  };

  let update = async (o: T) => {
    let res = await fetch(formatIDEndpoint(o.id), {
      body: JSON.stringify(o),
      method: "PUT",
    });

    // remove "id" from o, and send rest as search params
    let t: T["id"] | Exclude<T, "id"> = await res.json();
    console.log(t);
  };

  let del = async (id: T["id"]) => {
    let res = await fetch(formatIDEndpoint(id), {
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
