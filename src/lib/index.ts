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
  console.log("tick");
  const trains: App.Train[] = await (await fetch("http://localhost:5133/api/trains/")).json();
  const links: App.Link[] = await (await fetch("http://localhost:5133/api/links/")).json();

  for(const train of trains) {
    if(train.schedule === null) continue;
    if(train.link === null) throw new Error(`Error updating train ${train.id}: schedule is not null, but train link is.`);

    const route = train.schedule.originStation.train_route;

    let link: App.Link = await (await fetch(`http://localhost:5133/api/links/${route}?origin=${route}&dest=${train.link.destStation.name}`)).json();

    if(train.station != null) {
      const station: App.Station = await (await fetch(`http://localhost:5133/api/stations/${route}/${train.station.name}`)).json();
      console.log(`${timestamp - train.station_arrival!.getTime()} < ${station.loading_time} = ${timestamp - train.station_arrival!.getTime() < station.loading_time}`);
      if(timestamp - train.station_arrival!.getTime() < station.loading_time) continue;
    } else {
      console.log(`moving into station...`);
      console.log(`${timestamp - train.station_departure!.getTime()} < ${link.duration} = ${timestamp - train.station_departure!.getTime() < link.duration}`);
      if(timestamp - train.station_departure!.getTime() < link.duration) continue;
    }

    console.log('moving train...');

    // update train object in DB
    await fetch(formatAPIObject("http://localhost:5133/api/trains", moveTrain(train, links, timestamp)), {
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
  const links: App.Link[] = await (await fetch("http://localhost:5133/api/links/")).json();
  let link: App.Link | null = ticket.train.link;
  if(link == null) throw new Error(`Error getting ticket train arrival time: train ${ticket.train.id} link is null.`)

  let d = 0;

  while(ticket.train.station?.name !== ticket.train.schedule?.destStation.name) {
    if(!ticket.train.station?.name) {
      d += link.duration - (timestamp - ticket.train.station_departure!.getTime());
    } else {
      // if we're at origin or destination station, don't add loading time.
      if(ticket.train.station.name === ticket.origin.name || ticket.train.station.name === ticket.dest.name) break;
      d += ticket.train.station.loading_time - (timestamp - ticket.train.station_arrival!.getTime());
    }

    ticket.train = moveTrain(ticket.train, links, timestamp);
  }

  if(ticket.train.station?.name == ticket.train.schedule?.destStation.name && ticket.train.station?.name != ticket.origin.name) {
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
  const route = ticket.origin.train_route;
  let station = ticket.origin;

  const links: App.Link[] = await (await fetch(`http://localhost:5133/api/links/`)).json()

  // create predicate function based on inbound/outbound ticket status.
  // inbound trains move from dest -> origin on a link, so find link whose dest == ticket.origin
  // outbound trains move from origin -> dest, so find link whose origin = ticket.origin
  const p = ticket.direction == App.TrainDirection.INBOUND ? ((l: App.Link) => l.destStation.name === station.name) : ((l: App.Link) => l.originStation.name === station.name);

  let link = links.filter((l) => l.destStation.train_route == route && l.originStation.train_route === route).find(p);
  if(!link) throw new Error();

  let d = 0;

  while(station.name != ticket.dest.name) {
    d += station.loading_time + link.duration;

    // move station to other: inbound choose origin; outbound choose dest
    const nextStationName = ticket.direction == App.TrainDirection.INBOUND ? link.originStation.name : link.destStation.name;
    station = await (await fetch(`http://localhost:5133/api/stations/${route}/${nextStationName}`)).json() as App.Station;
    if(station === null) throw new Error();

    link = links.filter((l) => l.destStation.train_route == route && l.originStation.train_route === route).find(p);
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
const moveTrain = (train: App.Train, links: App.Link[], t: number): App.Train => {
  if(train.schedule === null) throw new Error();
  if(train.link === null) throw new Error();

  if(train.station === null) {
    // grab new link.
    // inbound trains move along link from dest to origin; next link is one whose dest === ticket.origin
    // outbound trains move along link from to dest; next link is one whose origin === ticket.origin

    const p = train.schedule.direction === App.TrainDirection.INBOUND ? ((l: App.Link) => l.destStation.name === train.link!.originStation.name) : ((l: App.Link) => l.originStation.name === train.link!.destStation.name);
    train.link = links.filter((l) => l.destStation.train_route == train.schedule!.originStation.train_route && l.originStation.train_route === train.schedule!.originStation.train_route).find(p)!;

    train.station = train.schedule.direction === App.TrainDirection.INBOUND ? train.link.originStation : train.link.destStation;
    train.station_arrival = new Date(t);
  } else {
    train.station = null;
    train.station_departure = new Date(t);
  }

  return train;
};
