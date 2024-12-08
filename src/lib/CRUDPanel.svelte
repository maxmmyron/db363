<script lang="ts" generics>
  let {
    requestSent,
    ids,
    nonids,
    endpoint,
    vals,
  }: {
    requestSent: () => void;
    ids: Array<string>;
    nonids: Array<string>;
    endpoint: string;
    vals: any;
  } = $props();

  if (endpoint.at(endpoint.length - 1) == "/") {
    throw new Error("endpoint param cannot end in '/'.");
  }

  const formatIDEndpoint = (id: object) => {
    let e = endpoint;
    let entries = Object.entries(id);
    if (entries.length === 0) return e;
    if (entries.length === 1 && entries.length === 1) e += `/${entries[0][1]}`;
    else {
      for (const [key, val] of entries) e += `?${key}=${val}&`;
      // cut last '&' off
      e.substring(0, e.length - 1);
    }

    return e;
  };

  let create = async (o: object) => {
    let res = await fetch(`${endpoint}/create`, {
      method: "POST",
      body: JSON.stringify(o),
    });
    let t = await res.json();
    console.log(t);
  };

  let read = async (id: object) => {
    let res = await fetch(formatIDEndpoint(id), {
      method: "GET",
    });
    let t = await res.json();
    console.log(t);
  };

  let update = async (o: object & { id: any }) => {
    let res = await fetch(formatIDEndpoint(o.id), {
      body: JSON.stringify(o),
      method: "PUT",
    });

    // remove "id" from o, and send rest as search params
    let t = await res.json();
    console.log(t);
  };

  let del = async (id: object) => {
    let res = await fetch(formatIDEndpoint(id), {
      method: "DELETE",
    });
    let t = await res.json();
    console.log(t);
  };
</script>

<div
  class="grid grid-rows-1"
  style="grid-template-columns: repeat({ids.length +
    nonids.length}, 1fr) 48px 48px;"
>
  {#each ids.concat(nonids) as k}
    <p>{k}</p>
  {/each}

  <form
    class="grid row-start-2 col-span-full grid-cols-subgrid"
    onsubmit={(e) => {
      e.preventDefault();
      let formData = new FormData(e.currentTarget);

      let o: { [key: string]: string } = {};
      formData.forEach((v, k) => {
        if (k === "id") return;
        o[k.toString()] = v.toString();
      });

      create(o).then(() => requestSent());
    }}
  >
    {#each ids as [k, v], i}
      {#if i === 0}
        <button
          type="submit"
          class="border px-2 py-0.5 w-full bg-black text-white font-bold col-start-1"
          style="grid-column-end:{ids.length + 1}">+</button
        >
      {/if}
    {/each}
    {#each Object.entries(nonids) as [k, v]}
      <!-- Skip if ID -->
      {#if k != "id"}
        <input name={k} type="text" class="border w-full" />
      {/if}
    {/each}
  </form>

  <hr class="row-start-3 col-span-full my-3" />

  {#each vals as val, i}
    <div
      class="grid col-span-full grid-cols-subgrid"
      style="grid-row-start: {i + 4};"
    >
      <form
        class="grid col-start-1 -col-end-2 grid-cols-subgrid"
        onsubmit={(e) => {
          e.preventDefault();
          let formData = new FormData(e.currentTarget);

          let o: { [key: string]: string } = {};
          formData.forEach((v, k) => {
            if (k === "id") return;
            o[k.toString()] = v.toString();
          });

          update(o as any).then(() => requestSent());
        }}
      >
        <!-- We can't guarantee ordering on objects here but not really ez way around that rn -->
        {#each ids.concat(nonids) as k}
          <input name={k} value={val[k]} class="border" />
        {/each}
        <button
          type="submit"
          class="border px-2 py-0.5 w-full bg-black text-white font-bold"
          >^</button
        >
      </form>

      <button
        class="border px-2 py-0.5 w-full bg-black text-white font-bold"
        onclick={(e) => {
          e.preventDefault();
          let id = new Map<String, string>();
          ids.forEach((k, _) => id.set(k, val[k]));
          del(id).then(() => requestSent());
        }}
      >
        x
      </button>
    </div>
  {/each}
</div>
