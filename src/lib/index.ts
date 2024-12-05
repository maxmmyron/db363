export const formatAPIObject = (endpoint: string, obj: Object) => {
  if(obj == null) return endpoint;
  if(endpoint.endsWith("/")) endpoint = endpoint.substring(0, endpoint.length - 1) + "?";
  else endpoint = endpoint + "?";
  Object.entries(obj).forEach(([key, val]) => endpoint += `${key}=${val}&`);
  return endpoint.substring(0, endpoint.length - 1);
}

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
  const links: App.Link[] = await (await fetch("http://localhost:5133/api/links")).json();

  for(const train of trains) {
    if(train.schedule_id === null) continue;
    const schedule: App.Schedule = await (await fetch(`http://localhost:5133/api/schedules/${train.schedule_id}`)).json();
    if(train.link_origin_name === null || train.link_dest_name === null) throw new Error(`Error updating train ${train.id}: schedule is not null, but train link is.`);

    let link: App.Link = await (await fetch(`http://localhost:5133/api/links/${schedule.origin_route}?origin=${train.link_origin_name}&dest=${train.link_dest_name}`)).json();

    if(train.station_name != null) {
      const station: App.Station = await (await fetch(`http://localhost:5133/api/stations/${schedule.origin_route}/${train.station_name}`)).json();
      if(timestamp - train.station_arrival!.getMilliseconds() < station.loading_time) continue;
    } else {
      if(timestamp - train.station_departure!.getMilliseconds() < link.duration) continue;
    }

    // update train object in DB
    await fetch(formatAPIObject("http://localhost:5133/api/trains", moveTrain(train, schedule, links, link, timestamp)), {
      method: "PUT"
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
  let train: App.Train = await(await fetch(`http://localhost:5133/api/trains${ticket.train_id}`)).json();
  if(train == null) throw new Error(`Error getting train arrival time for ticket: ${ticket.train_id} doesn't exist.`);

  let schedule: App.Schedule = await(await fetch(`http://localhost:5133/api/schedules${train.schedule_id}`)).json()
  if(schedule == null) throw new Error(`Error getting train arrival time for ticket: ${train.schedule_id} doesn't exist.`);

  const links: App.Link[] = await (await fetch("http://localhost:5133/api/links")).json();
  // initial link
  let link: App.Link = await (await fetch(`http://localhost:5133/api/links/${schedule.origin_route}?origin=${train.link_origin_name}&dest=${train.link_dest_name}`)).json();

  let d = 0;

  while(train.station_name !== schedule.dest_name) {
    if(train.station_name === null) {
      d += link.duration - (timestamp - train.station_departure!.getMilliseconds());
    } else {
      const station = await (await fetch(`http://localhost:5133/api/stations/${schedule.origin_route}/${train.station_name}`)).json() as App.Station;
      // if we're at origin or destination station, don't add loading time.
      if(train.station_name === ticket.origin_name || train.station_name === ticket.dest_name) break;
      d += station.loading_time - (timestamp - train.station_arrival!.getMilliseconds());
    }

    moveTrain(train, schedule, links, link, timestamp);
  }

  if(train.station_name == schedule.dest_name && train.station_name != ticket.origin_name) {
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
  const route = ticket.origin_route;

  let station = await (await fetch(`http://localhost:5133/api/stations/${route}/${ticket.origin_name}`)).json() as App.Station;
  if(station === null) throw new Error();

  const links: App.Link[] = await (await fetch(`http://localhost:5133/api/links`)).json()

  // create predicate function based on inbound/outbound ticket status.
  // inbound trains move from dest -> origin on a link, so find link whose dest == ticket.origin
  // outbound trains move from origin -> dest, so find link whose origin = ticket.origin
  const p = ticket.direction == App.TrainDirection.INBOUND ? ((l: App.Link) => l.dest_name === station.name) : ((l: App.Link) => l.origin_name === station.name);

  let link = links.filter((l) => l.dest_route == route && l.origin_route === route).find(p);
  if(!link) throw new Error();

  let d = 0;

  while(station.name != ticket.dest_name) {
    d += station.loading_time + link.duration;

    // move station to other: inbound choose origin; outbound choose dest
    const nextStationName = ticket.direction == App.TrainDirection.INBOUND ? link.origin_name : link.dest_name;
    station = await (await fetch(`http://localhost:5133/api/stations/${route}/${nextStationName}`)).json() as App.Station;
    if(station === null) throw new Error();

    link = links.filter((l) => l.dest_route == route && l.origin_route === route).find(p);
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
const moveTrain = (train: App.Train, schedule: App.Schedule, links: App.Link[], link: App.Link, t: number): App.Train => {
  if(train.station_name === null) {
    // grab new link.
    // inbound trains move along link from dest to origin; next link is one whose dest === ticket.origin
    // outbound trains move along link from to dest; next link is one whose origin === ticket.origin

    const p = schedule.direction === App.TrainDirection.INBOUND ? ((l: App.Link) => l.dest_name === link.origin_name) : ((l: App.Link) => l.origin_name === link.dest_name);
    link = links.filter((l) => l.dest_route == schedule.origin_route && l.origin_route === schedule.origin_route).find(p)!;

    train.station_name = schedule.direction === App.TrainDirection.INBOUND ? link.origin_name : link.dest_name;
    train.station_arrival = new Date(t);
    train.link_origin_name = link.origin_name;
    train.link_dest_name = link.dest_name;
  } else {
    train.station_name = null;
    train.station_departure = new Date(t);
  }

  return train;
};
