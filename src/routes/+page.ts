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
    trains: await trains.json() as App.Train[],
    passengers: await passengers.json() as App.Passenger[],
    schedules: await schedules.json() as App.Schedule[],
    tickets: await tickets.json() as App.Ticket[],
  };
}
