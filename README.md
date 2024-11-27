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

An individual train that may be given a schedule. Has the following relationships:

- nullable 1:1 w/ Station (One train _may_ be at one station; One station _may_ have one train at it.)
- M:1 w/ Schedule (A train is assigned one schedule; one schedule may have many trains assigned to it.)

| Name            | Type        | PK  | Description                                                                       |
| --------------- | ----------- | --- | --------------------------------------------------------------------------------- |
| id              |             | PRI |                                                                                   |
| schedule        | Schedule.id | FRN | The current schedule assigned to this train                                       |
| sched_departure | Time        |     | Departure time from initial station                                               |
| station         | Station.id  | FRN | The station the train is at. (May be null)                                        |
| arrival         | Time        |     | The time the train arrived at the station. NULL if the train is not at a station. |
| departure       | Time        |     | The time the train departed the station. NULL if the train is at a station.       |
| status          | String      |     | in route, arriving, boarding, departing, etc.                                     |

### Table: Station

A station that can house a train.

| Name         | Type   | PK  | Description                                    |
| ------------ | ------ | --- | ---------------------------------------------- |
| name         | String | PRI | Human-readable name of station                 |
| route        | String | PRI | The route assigned to this station (see below) |
| loading_time | Number |     | How long the train is at the station for       |

_Note:_ Route is described in this table so we can associate a station with multiple routes; e.g. Park Street -> Green and Red line

### Link

A connection between two stations via a train along a route. Used to describe time/distance for individual route components.

_NOTE: Primary key is composed of two composite foreign keys (one for origin station, one for destination station)._

| Name         | Type         | PK  | Description                                      |
| ------------ | ------------ | --- | ------------------------------------------------ |
| origin_name  | VARACHAR(64) | FRN |                                                  |
| origin_route | VARCHAR(48)  | FRN |                                                  |
| Terminus     | VARACHAR(64) | FRN |                                                  |
| dest_route   | VARACHAR(48) | FRN |                                                  |
| Duration     | Number       |     | The time it takes to go between the two stations |
| Distance     | NUmber       |     | The distance between the two stations            |

### Table: Schedule

A schedule that specifies a route a train may run along.

| Name         | Type           | PK  | Description        |
| ------------ | -------------- | --- | ------------------ |
| id           |                | PRI |                    |
| origin_name  | VARACHAR(64)   | FRN |                    |
| origin_route | VARCHAR(48)    | FRN |                    |
| dest_name    | VARACHAR(64)   | FRN |                    |
| dest_route   | VARACHAR(48)   | FRN |                    |
| dir          | TrainDirection |     | direction of train |

### Table: Ticket

A ticket specifies a passenger's single trip along a scheduled route. Has the following relationships:

- M:1 w/ Passenger (Many tickets can be allocated to a single passenger; one passenger may have many tickets)
- M:1 w/ Train (Many tickets may specify a single train; one train may have many tickets)

_NOTE: More complex trips (like those with interchanges) require more than one ticket._

| Name         | Type         | PK  | Description                               |
| ------------ | ------------ | --- | ----------------------------------------- |
| passenger    | Passenger.id | PRI | The passenger this ticket belongs to      |
| train        | Train.id     | PRI | The train to board for this ticket        |
| origin_name  | VARCHAR(64)  | FRN |                                           |
| origin_route | VARCHAR(48)  | FRN |                                           |
| dest_name    | VARCHAR(64)  | FRN |                                           |
| dest_route   | VARCHAR(48)  | FRN |                                           |
| departure    | Time         |     | When the train departs the origin station |

### Table: Passenger

A passenger. May have a ticket for a route.

Cascades:

- On Deletion: Any associated ticket should be deleted.

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
