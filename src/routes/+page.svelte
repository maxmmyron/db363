<script lang="ts">
  import CrudPanel from "$lib/CRUDPanel.svelte";
  import { tick } from "$lib/index";
  import type { PageData } from "./$types";
  import { time, DEFAULT_TIME } from "$lib/stores";

  let { data }: { data: PageData } = $props();

  let d = $state(data);
  $effect(() => console.log(d));

  $effect(() => {
    tick($time).then(async () => {
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
    if (!paused) $time += 1000 * 60;
  }, 1000);
</script>

{#if data?.err}
  <p>Error! {data.err}</p>
{/if}

<section>
  {#key time}
    <p class="">
      time: {new Date($time).toLocaleString("en-US", { timeZone: "UTC" })}
    </p>
  {/key}
  <button onclick={() => (paused = !paused)} class="border"
    >{paused ? "unpause" : "pause"}</button
  >
  <button onclick={() => ($time += 1000 * 60)} class="border">tick</button>

  <button
    onclick={() => ($time = DEFAULT_TIME)}
    class="border bg-red-700 color-white font-bold px-1">RESET TIME</button
  >

  {#if ziplines}
    {#each Object.entries(ziplines) as [line, { parts, color }]}
      <div class="w-full flex justify-center">
        <div
          class="grid grid-rows-[24px,12px,24px] mb-24 w-11/12"
          style="grid-template-columns: repeat({Math.ceil(parts.length / 2) -
            1}, 1fr);"
        >
          <div
            class="col-start-1 row-start-2 rounded-xl w-full z-0"
            style="background-color: {color}; grid-column-end: {Math.ceil(
              parts.length / 2
            ) + 1}"
          ></div>
          {#each parts as { part, isTransfer, trains }, i}
            {#if i % 2 === 0}
              <div
                class="row-span-full z-10 place-items-center {i ==
                parts.length - 1
                  ? 'flex justify-end pr-0.5 w-3'
                  : 'grid grid-rows-subgrid grid-cols-[12px,1fr]'}"
                style="grid-column-start: {i / 2 + 1};"
              >
                <!-- Station: Trains -->
                <div class="row-start-1 flex gap-1">
                  {#each trains as train}
                    <div
                      class="min-w-[12px] h-5 px-1 rounded-sm bg-neutral-800 shadow-sm flex items-center justify-center"
                    >
                      <p class="text-white font-bold">{train.id}</p>
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
                    class="absolute rotate-[50deg] origin-top-left top-1 {i ==
                    parts.length - 1
                      ? 'left-2'
                      : '-left-8'} text-nowrap text-neutral-800"
                  >
                    {(part as App.Station).name}
                  </p>
                </div>

                <!-- show link -->
                {#if i !== parts.length - 1}
                  {@const trains = parts[i + 1].trains}
                  <div class="row-start-1 col-start-2 flex gap-1">
                    {#each trains as train}
                      <div
                        class="min-w-[12px] h-5 px-1 rounded-sm bg-neutral-800 shadow-sm flex items-center justify-center"
                      >
                        <p class="text-white font-bold">{train.id}</p>
                      </div>
                    {/each}
                  </div>
                {/if}
              </div>
            {/if}
          {/each}
        </div>
      </div>
    {/each}
  {/if}
</section>

<section class="border rounded-sm">
  <h1 class="text-center">Passengers</h1>
  <hr />
  <CrudPanel
    vals={d.passengers}
    requestSent={async () => {
      let res = await fetch("http://localhost:5133/api/passengers/");
      d.passengers = await res.json();
    }}
    ids={["id"]}
    nonids={["firstName", "lastName"]}
    endpoint="http://localhost:5133/api/passengers"
  />
</section>

<section class="border rounded-sm">
  <h1 class="text-center">Trains</h1>
  <hr />
  <CrudPanel
    vals={d.trains}
    requestSent={async () => {
      let res = await fetch("http://localhost:5133/api/trains/");
      d.trains = await res.json();
    }}
    ids={["id"]}
    nonids={[
      "schedule",
      "station",
      "link",
      "schedDep",
      "stationArrival",
      "stationDep",
      "status",
    ]}
    endpoint="http://localhost:5133/api/trains"
  />
</section>

<section class="border rounded-sm">
  <h1 class="text-center">Schedules</h1>
  <hr />
  <CrudPanel
    vals={d.schedules}
    requestSent={async () => {
      let res = await fetch("http://localhost:5133/api/schedules/");
      d.schedules = await res.json();
    }}
    ids={["id"]}
    nonids={["origin", "dest", "direction"]}
    endpoint="http://localhost:5133/api/schedules"
  />
</section>

<section class="border rounded-sm">
  <h1 class="text-center">Tickets</h1>
  <hr />
  <CrudPanel
    vals={d.tickets}
    requestSent={async () => {
      let res = await fetch("http://localhost:5133/api/tickets/");
      d.tickets = await res.json();
    }}
    ids={["passenger", "train"]}
    nonids={["origin", "dest", "departure", "direction"]}
    endpoint="http://localhost:5133/api/tickets"
  />
</section>
