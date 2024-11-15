<script lang="ts">
  import type { PageData } from "./$types";

  let { data }: { data: PageData } = $props();

  let trains = $state(data.trains);
  $effect(() => console.log(trains));
  $effect(() => console.log(...trains));
</script>

<h1>trains</h1>
{#if data.err}
  <p>Error: {data.err}</p>
{:else}
  {data.trains}
{/if}
