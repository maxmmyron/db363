<script lang="ts">
  import type { PageData } from "./$types";

  let { data }: { data: PageData } = $props();
</script>

<h1>trains</h1>
{#if data.err}
  <p>Error: {data.err}</p>
{:else}
  {data.trains}
{/if}
