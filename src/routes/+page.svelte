<script lang="ts">
  import CrudPanel from "$lib/CRUDPanel.svelte";
  import { tick } from "$lib/index";
  import type { PageData } from "./$types";

  let { data }: { data: PageData } = $props();

  let d = $state(data);
  $effect(() => console.log(d));

  let time = $state(new Date("2024-01-01T08:00:00+00:00").getTime());
  $effect(() => {
    tick(time).then(async () => {
      console.log("tick complete... updating trains");
      let res = await fetch("http://localhost:5133/api/trains/");
      d.trains = await res.json();
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
      [route: string]: {
        color: string;
        parts: Array<{
          part: App.Station | App.Link;
          trains: App.Train[];
          isTransfer: boolean;
        }>;
      };
    } = {
      "Line 1": { color: "#8A4F7D", parts: [] },
      "Line 2": { color: "#F6CACA", parts: [] },
      "Line 3": { color: "#B3DEC1", parts: [] },
      "Line 4": { color: "#ECA400", parts: [] },
    };

    for (const [r, line] of Object.entries(lines)) {
      const { ordering } = orderings.find((o) => o.line === r)!;
      for (let i = 0; i < ordering.length; i++) {
        let namedStations = d.stations.filter((s) => s.name === ordering[i]);
        let station = namedStations.find((s) => s.route === r)!;
        let link = d.links.find(
          (s) => s.origin.route === r && s.origin.name === ordering[i]
        )!;

        // add new station
        line.parts.push({
          part: station,
          isTransfer: namedStations.length > 1,
          trains: d.trains.filter(
            (t) =>
              t.station?.name == station.name &&
              t.station?.route === station.route
          ),
        });

        // if we're not at last station, add new link
        if (i === ordering.length - 1) continue;
        line.parts.push({
          part: link,
          isTransfer: false,
          trains: d.trains.filter(
            (t) =>
              t.station === null &&
              t.link.origin.name === link.origin.name &&
              t.link.origin.route == link.origin.route
          ),
        });
      }
    }
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
    <p class="">
      time: {new Date(time).toLocaleString("en-US", { timeZone: "UTC" })}
    </p>
  {/key}
  <button onclick={() => (paused = !paused)} class="border"
    >{paused ? "unpause" : "pause"}</button
  >
  <button onclick={() => (time += 1000 * 60)} class="border">tick</button>

  {#if ziplines}
    {#each Object.entries(ziplines) as [line, { parts, color }]}
      <div class="grid grid-rows-3 h-9 grid-flow-col mb-24 w-full">
        <div
          class="col-start-1 row-start-2 rounded-xl w-full z-0"
          style="background-color: {color}; grid-column-end: {Math.ceil(
            parts.length / 2
          ) + 1}"
        ></div>
        {#each parts as { part, isTransfer, trains }, i}
          {#if i % 2 === 0}
            <div
              class="row-span-full grid grid-rows-subgrid z-10 place-items-center {i ==
              parts.length - 1
                ? 'grid-cols-[12px]'
                : 'grid-cols-[12px,1fr]'}"
              style="grid-column-start: {i / 2 + 1};"
            >
              <!-- Station: Trains -->
              <div class="row-start-1">
                {#each trains as train}
                  <div class="w-2 h-3 rounded-full bg-green-500">
                    {train.id}
                  </div>
                {/each}
              </div>
              <!-- Station styling -->
              {#if isTransfer}
                <div
                  class="w-4 h-4 rounded-full border-2 border-neutral-800 bg-white {i ==
                  parts.length - 1
                    ? 'justify-end'
                    : ''}"
                ></div>
              {:else}
                <div
                  class="w-2 h-2 rounded-full bg-white {i == parts.length - 1
                    ? 'justify-end'
                    : ''}"
                ></div>
              {/if}
              <div class="relative">
                <p
                  class="absolute rotate-[50deg] origin-top-left top-1 -left-5 text-nowrap text-neutral-800"
                >
                  {(part as App.Station).name}
                </p>
              </div>

              <!-- show link -->
              {#if i !== parts.length - 1}
                {@const trains = parts[i + 1].trains}
                <div class="row-start-1 col-start-2">
                  {#each trains as train}
                    <div class="w-2 h-3 rounded-full bg-green-500">
                      {train.id}
                    </div>
                  {/each}
                </div>
              {/if}
            </div>
          {/if}
        {/each}
      </div>
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
