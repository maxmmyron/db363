# CMPSC 363 Final Project

## Stack

- mysql (db)
- java spring (backend)
- svelte (front end)

## Requirements

- mysql server running on 3306

## Overview of setup:

On first load, Spring will look for schema.sql and data.sql files which set up the database:

> Spring Boot can automatically create the schema (DDL scripts) of your JDBC DataSource or R2DBC ConnectionFactory and initialize its data (DML scripts).
> By default, it loads schema scripts from optional:classpath*:schema.sql and data scripts from optional:classpath*:data.sql. The locations of these schema and data scripts can be customized using spring.sql.init.schema-locations and spring.sql.init.data-locations respectively. The optional: prefix means that the application will start even when the files do not exist. To have the application fail to start when the files are absent, remove the optional: prefix.

(see [Database Initialization -- Spring.io](https://docs.spring.io/spring-boot/how-to/data-initialization.html))

After loading the SQL db successfully, the spring application runs on port 5133, which front-end can communicate with.

## Database design

### Table: Train

> An individual train that may be given a schedule and route.

| Name    | Type       | Description                                  |
| ------- | ---------- | -------------------------------------------- |
| ID      |            |                                              |
| Route   | Route.id   | The route the train is currently assigned to |
| Station | Station.id | The station the train was last at.           |
| Status  | String     | in route, arriving, boarding, departing, etc |

### Table: Station

> A station that can house one or more trains

| Name         | Type     | Description                                    |
| ------------ | -------- | ---------------------------------------------- |
| ID           |          |                                                |
| Name         | String   | Human-readable name of station                 |
| Route        | Route.ID | The route assigned to this station (see below) |
| Inbound      | Link.ID  | The link to the previous station               |
| Outbound     | Link.ID  | The link to the following station              |
| Loading time | Number   | How long the train is at the station for       |

_Note:_ Route is described in this table so we can associate a station with multiple routes; e.g. Park Street -> Green and Red line

### Link

> A connection between two stations via a train along a route. Used to describe time/distance for individual route components.

| Name     | Type       | Description                                      |
| -------- | ---------- | ------------------------------------------------ |
| ID       |            |                                                  |
| Origin   | Station.id | The station this link starts at                  |
| Terminus | Station.id | The station this link ends at                    |
| Duration | Number     | The time it takes to go between the two stations |
| Distance | number     | The distance between the two stations            |

### Table: Route

> A route that may be assigned to a train. May be described as a series of stations.

| Name     | Type       | Description                          |
| -------- | ---------- | ------------------------------------ |
| ID       |            |                                      |
| Source   | Station.id | The genesis station along the route  |
| Dest     | Station.id | The terminal station along the route |
| Duration | Number     | The total duration of the route      |
| Distance | Number     | The total length of the route, in km |

### Table: Ticket

> A ticket is a single trip that a passenger may take between two stations. It is valid for a single trip on one route.

| Name           | Type         | Description                               |
| -------------- | ------------ | ----------------------------------------- |
| ID             |              |                                           |
| Passenger      | Passenger.id | The passenger this ticket belongs to      |
| Train          | Train.id     | The train to board for this ticket        |
| Source         | Station.id   |                                           |
| Dest           | Station.id   |                                           |
| Departure time | Time         | When the train departs the source station |
| Direction      | String       | Inbound, Outbound                         |

### Table: Passenger

> A passenger with a ticket for a route.

| Name  | Type   | Description |
| ----- | ------ | ----------- |
| ID    |        |             |
| First | String |             |
| Last  | String |             |

## Logic examples

### Getting the arrival time for a given ticket

```js
let time = tix.departure_time;
let station = tix.source_id;
while (station != tix.dest_id) {
  if (tix.dir == "inbound") {
    time += station.inbound_time + station.loading_time;
    station = station.inbound;
  } else {
    time += station.outbound_time + station.loading_time;
    station = station.inbound;
  }
}

return time;
```

## Project info

- `./src/main/resources/init.sql`: mysql setup file (ran on first run to init tables n whatnot)

## Troubleshooting

### Fails to connect: "Port in use" (dep. bc docker no longer used in project)

- `netstat -ano | findstr 5133`; note PID of task(s) using port
- `taskkill /F /PID <pid>`
