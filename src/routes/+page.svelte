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
      console.log("tick complete... updating trains");
      let res = await fetch("http://localhost:5133/api/trains/");
      data.trains = await res.json();
    });
  });

  let ziplines = $derived.by(() => {
    if (d.links == null) return [];

    const orderings = [
      {
        line: "Line 1",
        ordering: [
          "Waterfront",
          "City Hall",
          "Market Cross",
          "Central Park",
          "Union Square",
          "Tech Hub",
          "Downtown",
          "Harbor Point",
          "Shopping Mall",
          "Convention Center",
          "Sports Complex",
          "University",
          "Library",
          "Museum",
        ],
      },
      {
        line: "Line 2",
        ordering: [
          "Airport Express",
          "Business District",
          "Market Cross",
          "City Hall",
          "Arts Center",
          "Riverside",
          "Entertainment Hub",
          "Central Station",
          "Shopping Mall",
          "Harbor Point",
          "Zoo Park",
          "Ocean View",
          "Beach Terminal",
        ],
      },
      {
        line: "Line 3",
        ordering: [
          "North Station",
          "Science Park",
          "Botanical Garden",
          "Exhibition Center",
          "Financial District",
          "Innovation Park",
          "Tech Hub",
          "Central Station",
          "Entertainment Hub",
          "Stadium",
          "Olympic Park",
          "Concert Hall",
          "Theater District",
          "Media Center",
          "Grand Hotel",
          "Conference Center",
          "Food Court",
          "Shopping Plaza",
        ],
      },
      {
        line: "Line 4",
        ordering: [
          "West Terminal",
          "Industrial Park",
          "Tech Hub",
          "Innovation Park",
          "Medical Center",
          "Union Square",
          "Downtown",
          "East Station",
          "Residential Area",
          "School District",
          "Park & Ride",
        ],
      },
    ];

    let lines: {
      [route: string]: Array<{
        part: App.Station | App.Link;
        trains: App.Train[];
      }>;
    } = {
      "Line 1": [],
      "Line 2": [],
      "Line 3": [],
      "Line 4": [],
    };

    for (const [r, line] of Object.entries(lines)) {
      const { ordering } = orderings.find((o) => o.line === r)!;
      for (let i = 0; i < ordering.length; i++) {
        const n = ordering[i];
        let s = d.stations.find((s) => s.route === r && s.name === n)!;
        let l = d.links.find(
          (s) => s.origin.route === r && s.origin.name === n
        )!;
        line.push({
          part: s,
          trains: d.trains.filter(
            (t) => t.station?.name == s.name && t.station?.route === s.route
          ),
        });

        if (i === ordering.length - 1) continue;
        line.push({
          part: l,
          trains: d.trains.filter(
            (t) =>
              t.station === null &&
              t.link.origin.name === l.origin.name &&
              t.link.origin.route == l.origin.route
          ),
        });
      }
    }
    console.log(d.trains);
    console.log(lines);
    return lines;
  });

  let paused = $state(true);
  setInterval(() => {
    if (!paused) time += 1000 * 60;
  }, 1000);
</script>

{#if data?.err}
  <p>Error! {data.err}</p>
{/if}

<section>
  {#key time}
    <p class="">time: {new Date(time).toString()}</p>
  {/key}
  <button onclick={() => (paused = !paused)} class="border"
    >{paused ? "unpause" : "pause"}</button
  >
  <button onclick={() => (time += 1000 * 60)} class="border">tick</button>

  {#if ziplines}
    {#each Object.entries(ziplines) as [line, zip]}
      <section>
        <h1>{line}</h1>
        <div class="flex">
          {#each zip as leg, i}
            {#if i % 2 === 0}
              <div class="flex flex-col gap-1 items-center justify-center">
                {#each leg.trains as train}
                  <div class="flex gap-0.5 w-full justify-center">
                    <div class="w-2 h-4 rounded-md bg-blue">{train.id}</div>
                  </div>
                {/each}
                <div class="rounded-full w-4 h-4 bg-black"></div>
                <p>{(leg.part as App.Station).name}</p>
              </div>
            {:else}
              <div class="flex flex-col gap-1 items-center justify-center">
                {#each leg.trains as train}
                  <div class="flex gap-0.5 w-full justify-center">
                    <div class="w-2 h-4 rounded-md bg-blue">{train.id}</div>
                  </div>
                {/each}
                <div class="w-12 h-0.5 bg-neutral-600"></div>
                <p></p>
              </div>
            {/if}
          {/each}
        </div>
      </section>
    {/each}
  {/if}

  <h1 class="flex-grow">Passengers</h1>
  <div class="flex gap-2">
    <CrudPanel
      requestSent={async () => {
        let res = await fetch("http://localhost:5133/api/passengers/");
        d.passengers = await res.json();
      }}
      prim={{
        id: new Map<string, string>([["id", ""]]),
        first_name: "",
        last_name: "",
      }}
      endpoint="http://localhost:5133/api/passengers"
    />
    <aside class="w-1/2 flex-grow">
      {#each d.passengers as App.Passenger[] as p}
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
        d.trains = await res.json();
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
      {#each d.trains as App.Train[] as t}
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
        d.schedules = await res.json();
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
      {#each d.schedules as App.Schedule[] as s}
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
        d.tickets = await res.json();
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
      {#each d.tickets as App.Ticket[] as tx}
        <pre style="font-family: monospace;">{JSON.stringify(tx)}</pre>
      {/each}
    </aside>
  </div>
</section>
