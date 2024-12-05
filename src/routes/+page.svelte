<script lang="ts">
  import CrudPanel from "$lib/CRUDPanel.svelte";
  import { tick } from "$lib/index";
  import type { PageData } from "./$types";

  let { data }: { data: PageData } = $props();

  let d = $state(data);
  $effect(() => console.log(d));

  let time = $state(new Date("2024-01-01T08:00:00").getTime());
  $effect(() => {
    tick(time).then(async () => {
      let res = await fetch("http://localhost:5133/api/passengers/");
      data.passengers = await res.json();
    });
  });

  let paused = true;

  setInterval(() => {
    if (!paused) time += 1000 * 60;
  }, 1000);
</script>

{#if data?.err}
  <p>Error! {data.err}</p>
{/if}

<section>
  {#key time}
    <p class="">time: {new Date(time).toUTCString()}</p>
  {/key}
  <button onclick={() => (paused = !paused)}
    >{paused ? "unpause" : "pause"}</button
  >
  <h1 class="flex-grow">Passengers</h1>
  <div class="flex gap-2">
    <CrudPanel
      requestSent={async () => {
        let res = await fetch("http://localhost:5133/api/passengers/");
        data.passengers = await res.json();
      }}
      prim={{
        id: new Map<string, string>([["id", ""]]),
        first_name: "",
        last_name: "",
      }}
      endpoint="http://localhost:5133/api/passengers"
    />
    <aside class="w-1/2 flex-grow">
      {#each data.passengers as App.Passenger[] as p}
        <pre style="font-family: monospace;">{JSON.stringify(p)}</pre>
      {/each}
    </aside>
  </div>
</section>

<section>
  <h1 class="flex-grow">Trains</h1>
  <div class="flex gap-2">
    <CrudPanel
      requestSent={async () => {
        let res = await fetch("http://localhost:5133/api/trains/");
        data.trains = await res.json();
      }}
      prim={{
        id: new Map<string, string>([["id", ""]]),
        schedule_id: "",
        schedule_route: "",
        schedule_departure: "",
        station_name: "",
        station_arrival: "",
        station_departure: "",
        status: "",
      }}
      endpoint="http://localhost:5133/api/trains"
    />
    <aside class="w-1/2 flex-grow">
      {#each data.trains as App.Train[] as t}
        <pre style="font-family: monospace;">{JSON.stringify(t)}</pre>
      {/each}
    </aside>
  </div>
</section>

<section>
  <h1 class="flex-grow">Schedules</h1>
  <div class="flex gap-2">
    <CrudPanel
      requestSent={async () => {
        let res = await fetch("http://localhost:5133/api/schedules/");
        data.schedules = await res.json();
      }}
      prim={{
        id: new Map<string, string>([["id", ""]]),
        origin_name: "",
        origin_route: "",
        dest_name: "",
        dest_route: "",
        direction: "",
      }}
      endpoint="http://localhost:5133/api/schedules"
    />
    <aside class="w-1/2 flex-grow">
      {#each data.schedules as App.Schedule[] as s}
        <pre style="font-family: monospace;">{JSON.stringify(s)}</pre>
      {/each}
    </aside>
  </div>
</section>

<section>
  <h1 class="flex-grow">Tickets</h1>
  <div class="flex gap-2">
    <CrudPanel
      requestSent={async () => {
        let res = await fetch("http://localhost:5133/api/tickets/");
        data.tickets = await res.json();
      }}
      prim={{
        id: new Map<string, string>([
          ["passenger_id", ""],
          ["train_id", ""],
        ]),
        departure: "",
        origin_name: "",
        origin_route: "",
        dest_name: "",
        dest_route: "",
        direction: "",
      }}
      endpoint="http://localhost:5133/api/tickets"
    />
    <aside class="w-1/2 flex-grow">
      {#each data.tickets as App.Ticket[] as tx}
        <pre style="font-family: monospace;">{JSON.stringify(tx)}</pre>
      {/each}
    </aside>
  </div>
</section>
