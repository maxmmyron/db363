<script lang="ts" generics="T extends {id: any, [k: string]: any}">
  import { formatAPIObject } from "$lib";

  export let prim: T;
  export let endpoint: string;

  let create = async (o: Exclude<T, "id">) => {
    let res = await fetch(formatAPIObject(`${endpoint}/create`, o), {
      method: "POST",
    });
    let t: T["id"] | Exclude<T, "id"> = await res.json();
    console.log(t);
  };

  let read = async (id: Partial<T["id"]>) => {
    let e = endpoint;
    for (const [_, val] of Object.entries(id)) e += `/${val}`;
    let res = await fetch(e, {
      method: "GET",
    });
    let t: T["id"] | Exclude<T, "id"> = await res.json();
    console.log(t);
  };

  let update = async (o: Partial<Exclude<T, "id">>) => {
    let res = await fetch(formatAPIObject(endpoint, o), {
      method: "PUT",
    });
    let t: T["id"] | Exclude<T, "id"> = await res.json();
    console.log(t);
  };

  let del = async (id: T["id"]) => {
    let e = endpoint;
    for (const [_, val] of Object.entries(id)) e += `/${val}`;
    let res = await fetch(e, {
      method: "DELETE",
    });
    let t: T["id"] | Exclude<T, "id"> = await res.json();
    console.log(t);
  };
</script>

<div>
  <section>
    <h1>Create</h1>
    <form
      on:submit={(e) => {
        e.preventDefault();
        let formData = new FormData(e.currentTarget);

        let o: { [key: string]: string } = {};
        formData.forEach((v, k) => {
          if (k === "id") return;
          o[k.toString()] = v.toString();
        });

        create(o as Exclude<T, "id">);
      }}
    >
      {#each Object.entries(prim) as [k, v]}
        <!-- Skip if ID -->
        {#if k != "id"}
          <input name={k} type="text" />
        {/if}
      {/each}
      <button type="submit">Submit</button>
    </form>
  </section>
  <section>
    <h1>Read</h1>
    <form
      on:submit={(e) => {
        e.preventDefault();
        let formData = new FormData(e.currentTarget);

        let id: { [key: string]: string } = {};
        formData.forEach((v, k) => (id[k.toString()] = v.toString()));

        read(id as Partial<T["id"]>);
      }}
    >
      {#each Object.entries(prim["id"]) as [k, v]}
        <input name={k} type="text" />
      {/each}
      <button type="submit">Submit</button>
    </form>
  </section>
  <section>
    <h1>Update</h1>
    <form
      on:submit={(e) => {
        e.preventDefault();
        let formData = new FormData(e.currentTarget);

        let o: { [key: string]: string } = {};
        formData.forEach((v, k) => {
          if (k === "id") return;
          o[k.toString()] = v.toString();
        });

        update(o as Partial<Exclude<T, "id">>);
      }}
    >
      {#each Object.entries(prim) as [k, v]}
        <!-- Skip if ID -->
        {#if k != "id"}
          <input name={k} type="text" />
        {/if}
      {/each}
      <button type="submit">Submit</button>
    </form>
  </section>
  <section>
    <h1>Delete</h1>
    <form
      on:submit={(e) => {
        e.preventDefault();
        let formData = new FormData(e.currentTarget);
        let id: { [key: string]: string } = {};
        formData.forEach((v, k) => (id[k.toString()] = v.toString()));

        del(id as T["id"]);
      }}
    >
      {#each Object.entries(prim["id"]) as [k, v]}
        <input name={k} type="text" />
      {/each}
      <button type="submit">Submit</button>
    </form>
  </section>
</div>
