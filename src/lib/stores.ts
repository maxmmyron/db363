import { writable } from "svelte/store";
import { browser } from "$app/environment";

export const DEFAULT_TIME = new Date("2024-01-01T08:00:00+00:00").getTime();
export const time = writable(browser && Number.parseInt(localStorage.getItem("time") ?? DEFAULT_TIME.toString()) || DEFAULT_TIME);

time.subscribe((t) => {
  if (browser) return (localStorage.time = t)
});
