import { error, redirect } from "@sveltejs/kit";
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

  let trainsJSON: App.Train[], passengersJSON: App.Passenger[], schedulesJSON: App.Schedule[], ticketsJSON: App.Ticket[];

  try {
    trainsJSON = await trains.json()
  } catch(err: any) {
    throw error(400, `Train JSON Parse err: ${err}`);
  }

  try {
    passengersJSON = await passengers.json()
  } catch(err: any) {
    throw error(400, `Train JSON Parse err: ${err}`);
  }

  try {
    schedulesJSON = await schedules.json()
  } catch(err: any) {
    throw error(400, `Train JSON Parse err: ${err}`);
  }

  try {
    ticketsJSON = await tickets.json()
  } catch(err: any) {
    throw error(400, `Train JSON Parse err: ${err}`);
  }

  return {
    trains: trainsJSON,
    passengers: passengersJSON,
    schedules: schedulesJSON,
    tickets: ticketsJSON,
  };
}
