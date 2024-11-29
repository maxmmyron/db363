import type { PageLoad } from "./$types"

export const load: PageLoad = async ({ params, fetch }) => {
  let trains = null, passengers = null, schedules = null, tickets = null;
  try {
    trains = await fetch("http://localhost:5133/api/trains/");
    passengers = await fetch("http://localhost:5133/api/passengers/");
    schedules = await fetch("http://localhost:5133/api/schedules/");
    tickets = await fetch("http://localhost:5133/api/tickets/");
  } catch (err: any) {
    return {
      trains: null,
      passengers: null,
      schedules: null,
      tickets: null,
      err,
    };
  }

  return {
    trains: await trains.json(),
    passengers: await passengers.json(),
    schedules: await schedules.json(),
    tickets: await tickets.json(),
  };
}
