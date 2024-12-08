/**
 * Updates each train according to the following rules:
 *   - If the train has no schedule: continue.
 *   - If the train is in transit, and the time in transit > link's transit time:
 *     - move train to next station, update link and arrival time
 *   - If the train is at a station, and the time at station >= station's loading time:
 *     - move train to transit, update departure time.
 * @param timestamp
 */
export const tick = async (timestamp: number) => {
  const trains: App.Train[] = await (await fetch("http://localhost:5133/api/trains/")).json();
  const links: App.Link[] = await (await fetch("http://localhost:5133/api/links/")).json();

  for(const train of trains) {
    if(train.schedule === null) continue;

    const route = train.schedule.origin.route;
    let link: App.Link = await (await fetch(`http://localhost:5133/api/links/${route}?origin=${train.link.origin.name}&dest=${train.link.dest.name}`)).json();

    // *don't* update train position if:
    // 1. we're at origin station and dt - scheduled departure > 0
    // 2. we're at any other station, and dt - station arrival < station loading time
    // 3. we're not at a station, and dt - station departure < travel time along link
    if(train.station != null) {
      if(train.station.name === train.schedule.origin.name) {
        console.log("at origin station...")
        console.log(`${timestamp} - ${Date.parse(train.schedDep + "+00:00")} (${timestamp - Date.parse(train.schedDep + "+00:00")}) <= 0`)
        if(timestamp - Date.parse(train.schedDep + "+00:00") <= 0) continue;
      } else {
        console.log("at a non-origin station...")
        console.log(`${timestamp} - ${Date.parse(train.stationArrival! + "+00:00")} (${timestamp - Date.parse(train.stationArrival! + "+00:00")}) <= ${train.station.loadingTime * 60000}`)
        if (timestamp - Date.parse(train.stationArrival! + "+00:00") < train.station.loadingTime * 60000) continue;
      }
    } else {
      console.log("in transit...")
      console.log(`${timestamp} - ${Date.parse(train.stationDep! + "+00:00")} (${timestamp - Date.parse(train.stationDep! + "+00:00")}) <= ${link.duration * 60000}`)
      if(timestamp - Date.parse(train.stationDep! + "+00:00") < link.duration * 60000) continue;
    }

    const nt = await moveTrain(train, links, timestamp);
    // update train object in DB
    await fetch(`http://localhost:5133/api/trains/${train.id}`, {
      method: "PUT",
      headers: {
        'Content-Type': "application/json"
      },
      body: JSON.stringify(nt),
    });
  }
};

/**
 * Calculates the time before a train reaches the boarding platform for a given ticke.
 *
 * @param ticket the ticket to get train's arrival time for
 * @param timestamp Current timestamp, for calculating initial station/transit time offset
 * @returns number of milliseconds before ticket's associated train reaches platform; -1 if train has already departed platform
 *
 * @throws `Error` if train does not exist for ticket
 * @throws `Error` if train does not have a schedule
 * @throws `Error` if next station link cannot be determined
 */
export const getTrainArrivalTime = async (ticket: App.Ticket, timestamp: number): Promise<number> => {
  const links: App.Link[] = await (await fetch("http://localhost:5133/api/links/")).json();

  let d = 0;
  while(ticket.train.station?.name !== ticket.train.schedule?.dest.name) {
    if(!ticket.train.station?.name) {
      d += ticket.train.link.duration - (timestamp - Date.parse(ticket.train.stationDep! + "+00:00"));
    } else {
      // if we're at origin or destination station, don't add loading time.
      if(ticket.train.station.name === ticket.origin.name || ticket.train.station.name === ticket.dest.name) break;
      d += ticket.train.station.loadingTime - (timestamp - Date.parse(ticket.train.stationArrival! + "+00:00"));
    }

    ticket.train = await moveTrain(ticket.train, links, timestamp);
  }

  if(ticket.train.station?.name == ticket.train.schedule?.dest.name && ticket.train.station?.name != ticket.origin.name) {
    return -1;
  }

  return d;
};

/**
 * Gets the total travel time for the ticket.
 *
 * @param ticket the ticket to get train's arrival time for
 * @param timestamp Current timestamp, for calculating initial station/transit time offset
 */
export const getTicketTransitTime = async (ticket: App.Ticket, timestamp: number): Promise<number> => {
  const route = ticket.origin.route;
  let station = ticket.origin;

  const links: App.Link[] = await (await fetch(`http://localhost:5133/api/links/`)).json()

  // create predicate function based on inbound/outbound ticket status.
  // inbound trains move from dest -> origin on a link, so find link whose dest == ticket.origin
  // outbound trains move from origin -> dest, so find link whose origin = ticket.origin
  const p = ticket.direction === "INBOUND" ? ((l: App.Link) => l.dest.name === station.name) : ((l: App.Link) => l.origin.name === station.name);

  let link = links.filter((l) => l.dest.route == route && l.origin.route === route).find(p);
  if(!link) throw new Error();

  let d = 0;

  while(station.name != ticket.dest.name) {
    d += station.loadingTime + link.duration;

    // move station to other: inbound choose origin; outbound choose dest
    const nextStationName = ticket.direction === "INBOUND" ? link.origin.name : link.dest.name;
    station = await (await fetch(`http://localhost:5133/api/stations/${route}/${nextStationName}`)).json() as App.Station;
    if(station === null) throw new Error();

    link = links.filter((l) => l.dest.route == route && l.origin.route === route).find(p);
    if(!link) throw new Error();
  }

  return 0;
};

/**
 * Moves a train along scheduled route
 *
 * TODO: what do if train at scheduled end of route???
 *
 * @param train The train to move
 * @param schedule The schedule the train is on
 * @param links A list of all links. Is filtered
 * @param link The link the train is currently on
 * @param t The current timestamp
 *
 * @returns modified train object
 */
const moveTrain = async (train: App.Train, links: App.Link[], t: number): Promise<App.Train> => {
  if(train.schedule === null) throw new Error("Couldn't move train: no schedule found.");
  if(train.link === null) throw new Error("Couldn't move train: no link found");

  if(train.station === null) {
    // grab new link.
    // inbound trains move along link from dest to origin; next link is one whose dest === ticket.origin
    // outbound trains move along link from to dest; next link is one whose origin === ticket.origin

    train.station = train.schedule.direction === "INBOUND" ? train.link.origin : train.link.dest;
    train.status = "BOARDING";

    // if we're at final schedule station, move train
    if(train.station.name === (train.schedule.direction === "INBOUND" ? train.schedule.origin : train.schedule.dest).name) return await reverseTrain(train, t);

    const p = train.schedule.direction === "INBOUND" ? ((l: App.Link) => l.dest.name === train.link.origin.name) : ((l: App.Link) => l.origin.name === train.link.dest.name);
    train.link = links.filter((l) => l.dest.route == train.schedule!.origin.route && l.origin.route === train.schedule!.origin.route).find(p)!;
    train.stationArrival = new Date(t).toISOString();
  } else {
    train.station = null;
    train.stationDep = new Date(t).toISOString();
    train.status = "IN TRANSIT";
  }

  return train;
};

/**
 * Reverses a train on a schedule. Requires the trains schedule to have an exact reverse.
 * @param train The train to reverse
 * @param t timestamp
 *
 * @throws if schedule doesn't have exact reverse
 */
const reverseTrain = async (train: App.Train, t: number): Promise<App.Train> => {
  if(train.schedule === null) throw new Error("Train is required to have a schedule to reverse");
  if(train.stationArrival === null) throw new Error("Error reversing train schedule: Train has no terminal station arrival time!")
  // find reverse schedule
  let schedules = await (await fetch(`http://localhost:5133/api/schedules/`)).json() as App.Schedule[];

  let rev = schedules.filter(s => s.origin.route === train.schedule?.origin.route).find(s => train.schedule?.origin.name === s.dest.name && train.schedule.dest.name === s.origin.name);
  if(rev === undefined) throw new Error("Error reversing train schedule: train's current schedule has no reverse");

  train.schedule = rev;
  train.schedDep = new Date(train.stationArrival + 1000*60*10 + ":00:00").toISOString()
  train.stationArrival = null;
  train.stationDep = null;

  return train;
};
