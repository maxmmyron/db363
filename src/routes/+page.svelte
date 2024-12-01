<script lang="ts">
  import CrudPanel from "$lib/CRUDPanel.svelte";
  import type { PageData } from "./$types";

  let { data }: { data: PageData } = $props();

  let d = $state(data);
  $effect(() => console.log(d));

  let createPassenger = async (f: string, l: string) => {
    let res = await fetch(
      `http://localhost:5133/api/passengers/create?first_name=${f}&last_name=${l}`,
      {
        method: "POST",
      }
    );
    console.log(res);
    return res;
  };
</script>

<button onclick={() => createPassenger("Steve", "Jobs")}> create</button>

{#if data?.err}<p>error! {data.err}</p>{:else}
  <h1>Trains</h1>
  {#each data.trains as App.Train[] as t}
    <pre style="font-family: monospace;">{JSON.stringify(t)}</pre>
  {/each}
  <h1>passengers</h1>
  {#each data.passengers as App.Passenger[] as p}
    <pre style="font-family: monospace;">{JSON.stringify(p)}</pre>
  {/each}
  <h1>schedules</h1>
  {#each data.schedules as App.Schedule[] as s}
    <pre style="font-family: monospace;">{JSON.stringify(s)}</pre>
  {/each}
  <h1>tix</h1>
  {#each data.tickets as App.Ticket[] as tx}
    <pre style="font-family: monospace;">{JSON.stringify(tx)}</pre>
  {/each}
{/if}

<CrudPanel
  prim={{
    id: new Map<string, string>([["id", ""]]),
    first_name: "",
    last_name: "",
  }}
  endpoint="http://localhost:5133/api/passengers"
/>
