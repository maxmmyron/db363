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
  // update train locations
  const trains = await (await fetch("http://localhost:5133/api/trains/")).json() as App.Train[];

  for(const train of trains) {
    if(train.schedule_id === null) continue;

    const route = train.schedule_route;

    if(train.link_origin === null || train.link_dest === null) throw new Error(`Error updating train ${train.id}: schedule is not null, but train link is.`);

    const link = await (await fetch(`http://localhost:5133/api/links/${route}?origin=${train.link_origin}&dest=${train.link_dest}`)).json() as App.Link;

    if(train.station_name != null) {
      const station = await (await fetch(`http://localhost:5133/api/stations/${route}/${train.station_name}`)).json() as App.Station;
      if(timestamp - train.station_arrival!.getMilliseconds() < station.loading_time) continue;

      // update train from station -> in transit
      train.station_name = null;
      train.station_departure = new Date(timestamp);
    } else {
      if(timestamp - train.station_departure!.getMilliseconds() < link.duration) continue;

      train.station_name = link.dest_name;
      train.station_arrival = new Date(timestamp);

      const links: App.Link[] = await (await fetch("http://localhost:5133/api/links")).json()
      const newLink: App.Link | undefined = links
        .filter((l) => l.dest_route == route && l.origin_route === route)
        .find((l) => l.dest_name !== link.origin_name || l.origin_name !== link.origin_name);

      if(!newLink) throw new Error(`Error updating train ${train.id}: No new link found for departure (old link ${link.origin_name} -> ${link.dest_name})`);

      train.link_dest = link.dest_name;
      train.link_origin = link.origin_name;
    }

    await fetch(formatAPIObject("http://localhost:5133/api/trains", train), {
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

  const route = train.schedule_route;
  let d = 0;

  while(train.station_name !== schedule.dest_name) {
    const link = await (await fetch(`http://localhost:5133/api/links/${route}?origin=${train.link_origin}&dest=${train.link_dest}`)).json() as App.Link;

    // transit -> station.
    if(train.station_name === null) {
      d += link.duration - (timestamp - train.station_departure!.getMilliseconds());

      train.station_name = link.dest_name;
      train.station_arrival = new Date(timestamp);

      const links: App.Link[] = await (await fetch("http://localhost:5133/api/links")).json()
      const newLink: App.Link | undefined = links
        .filter((l) => l.dest_route == route && l.origin_route === route)
        .find((l) => l.dest_name !== link.origin_name || l.origin_name !== link.origin_name);

      if(!newLink) throw new Error(`Error updating train ${train.id}: No new link found for departure (old link ${link.origin_name} -> ${link.dest_name})`);

      train.link_dest = link.dest_name;
      train.link_origin = link.origin_name;
    }

    // station -> transit. if train *started* in transit, previous branch execs which moves train to station
    if(train.station_name !== null) {
      const station = await (await fetch(`http://localhost:5133/api/stations/${route}/${train.station_name}`)).json() as App.Station;

      // if we're at station, don't add loading time.
      if(train.station_name == ticket.origin_name) break;
      d += station.loading_time - (timestamp - train.station_arrival!.getMilliseconds());

      train.station_name = null;
      train.station_departure = new Date(timestamp);
    }
  }

  if(train.station_name == schedule.dest_name && train.station_name != ticket.origin_name) {
    return -1;
  }

  return d;
};
