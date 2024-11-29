<script lang="ts">
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
  {#each data.trains as t}
    <pre style="font-family: monospace;">{JSON.stringify(t)}</pre>
  {/each}
  <h1>passengers</h1>
  {#each data.passengers as p}
    <pre style="font-family: monospace;">{JSON.stringify(p)}</pre>
  {/each}
  <h1>schedules</h1>
  {#each data.schedules as s}
    <pre style="font-family: monospace;">{JSON.stringify(s)}</pre>
  {/each}
  <h1>tix</h1>
  {#each data.tickets as tx}
    <pre style="font-family: monospace;">{JSON.stringify(tx)}</pre>
  {/each}
{/if}
