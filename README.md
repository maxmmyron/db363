# CMPSC 363 Final Project

## Stack

- mysql (db)
- java spring (back end)
- SvelteKit (front end)

## Requirements

- mysql server running on 3306
- node.js (v18.13 or above)

## setup:

1. Clone package

2. `npm install`

3. start MySQL server (_on port 3306_)

4. start DB back-end (at `src/main/java/com/mmyron/db363/Db363Application.java`)

5. start front end (w/ `npm run dev -- --open`)

---

On first load, Spring will look for `./src/main/resources/init.sql` which will set up the initial database content:

> Spring Boot can automatically create the schema (DDL scripts) of your JDBC DataSource or R2DBC ConnectionFactory and initialize its data (DML scripts).
> By default, it loads schema scripts from optional:classpath*:schema.sql and data scripts from optional:classpath*:data.sql. The locations of these schema and data scripts can be customized using spring.sql.init.schema-locations and spring.sql.init.data-locations respectively. The optional: prefix means that the application will start even when the files do not exist. To have the application fail to start when the files are absent, remove the optional: prefix.

(see [Database Initialization -- Spring.io](https://docs.spring.io/spring-boot/how-to/data-initialization.html))

After loading the SQL db successfully, the spring application runs on port 5133, which front-end can communicate with.

## Database Design

### Table: Train

An individual train that may be given a schedule.

| Name               | Type          | PK  | Description                                                                              |
| ------------------ | ------------- | --- | ---------------------------------------------------------------------------------------- |
| id                 |               | PRI |                                                                                          |
| schedule_id        | Schedule.id   | FRN | The current schedule assigned to this train                                              |
| schedule_departure | LocalDateTime |     | DepartureLocalDateTimefrom initial station                                               |
| station_name       | String        | FRN | The station the train is at. (May be null)                                               |
| station_route      | String        | FRN | The station the train is at. (May be null)                                               |
| station_arrival    | LocalDateTime |     | TheLocalDateTimethe train arrived at the station. NULL if the train is not at a station. |
| station_departure  | LocalDateTime |     | TheLocalDateTimethe train departed the station. NULL if the train is at a station.       |
| link_origin_name   | String        | FRN |                                                                                          |
| link_origin_route  | String        | FRN |                                                                                          |
| link_dest_name     | String        | FRN |                                                                                          |
| link_dest_route    | String        | FRN |                                                                                          |
| train_status       | String        |     | in route, arriving, boarding, departing, etc.                                            |

**Relationships:**

- nullable 1:1 w/ Station (One train _may_ be at one station; One station _may_ have one train at it.)
- M:1 w/ Schedule (A train is assigned one schedule; one schedule may have many trains assigned to it.)

**Functional dependencies:**

$$
R(\text{id}, \text{schedule\textunderscore id}, \text{schedule\textunderscore departure}, \text{station\textunderscore name}, \text{station\textunderscore route}, \text{station\textunderscore arrival}, \text{station\textunderscore departure}, \text{link\textunderscore origin\textunderscore name}, \text{link\textunderscore origin\textunderscore route}, \text{link\textunderscore dest\textunderscore name}, \text{link\textunderscore dest\textunderscore route}, \text{train\textunderscore status})\\\
\text{id} \rarr (\text{schedule\textunderscore id}, \text{schedule\textunderscore departure}, \text{station\textunderscore name}, \text{station\textunderscore route}, \text{station\textunderscore arrival}, \text{station\textunderscore departure}, \text{link\textunderscore origin\textunderscore name}, \text{link\textunderscore origin\textunderscore route}, \text{link\textunderscore dest\textunderscore name}, \text{link\textunderscore dest\textunderscore route}, \text{train\textunderscore status})
$$

**Demonstration of normal form:**
The table is in 3NF because each non-key column is directly tied to the PK, and there are no transitive dependencies.

---

### Table: Station

A station that can house a train.
_NOTE: Route is described in this table so we can associate a station with multiple routes; e.g. Park Street -> Green and Red line_

| Name         | Type   | PK  | Description                                    |
| ------------ | ------ | --- | ---------------------------------------------- |
| name         | String | PRI | Human-readable name of station                 |
| train_route  | String | PRI | The route assigned to this station (see below) |
| loading_time | Number |     | How long the train is at the station for       |

$$
R(\text{name}, \text{train\textunderscore route}, \text{loading\textunderscore time})\\\
(\text{name}, \text{train\textunderscore route}) \rarr (\text{loading\textunderscore time})
$$

**Functional dependencies:**
TODO:

**Demonstration of normal form:**
The table is in 3NF because each non-key column is directly tied to the PK, and there are no transitive dependencies.

---

### Link

A connection between two stations via a train along a route. Used to describe time/distance for individual route components.

_NOTE: Primary key is composed of two composite foreign keys (one for origin station, one for destination station)._

| Name         | Type         | PK  | Description                                             |
| ------------ | ------------ | --- | ------------------------------------------------------- |
| origin_name  | VARACHAR(64) | FRN |                                                         |
| origin_route | VARCHAR(48)  | FRN |                                                         |
| dest_name    | VARACHAR(64) | FRN |                                                         |
| dest_route   | VARACHAR(48) | FRN |                                                         |
| duration     | Number       |     | TheLocalDateTimeit takes to go between the two stations |
| distance     | NUmber       |     | The distance between the two stations                   |

**Functional dependencies:**

$$
R(\text{origin\textunderscore name},\text{origin\textunderscore route},\text{dest\textunderscore name},\text{dest\textunderscore route},\text{duration},\text{distance})\\\
(\text{origin\textunderscore name},\text{origin\textunderscore route},\text{dest\textunderscore name},\text{dest\textunderscore route}) \rarr (\text{duration},\text{distance})
$$

**Demonstration of normal form:**
The table is in 3NF because each non-key column is directly tied to the PK, and there are no transitive dependencies.

---

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

**Functional dependencies:**

$$
R(\text{id}, \text{origin\textunderscore name},\text{origin\textunderscore route},\text{dest\textunderscore name},\text{dest\textunderscore route},\text{dir})\\\
(\text{id}) \rarr (\text{origin\textunderscore name},\text{origin\textunderscore route},\text{dest\textunderscore name},\text{dest\textunderscore route},\text{dir})
$$

**Demonstration of normal form:**
The table is in 3NF because each non-key column is directly tied to the PK, and there are no transitive dependencies.

---

### Table: Ticket

A ticket specifies a passenger's single trip along a scheduled route.
_NOTE: More complex trips (like those with interchanges) require more than one ticket._

| Name         | Type          | PK  | Description                               |
| ------------ | ------------- | --- | ----------------------------------------- |
| passenger_id | Passenger.id  | PRI | The passenger this ticket belongs to      |
| train_id     | Train.id      | PRI | The train to board for this ticket        |
| origin_name  | VARCHAR(64)   | FRN |                                           |
| origin_route | VARCHAR(48)   | FRN |                                           |
| dest_name    | VARCHAR(64)   | FRN |                                           |
| dest_route   | VARCHAR(48)   | FRN |                                           |
| departure    | LocalDateTime |     | When the train departs the origin station |

**Relationships:**

- M:1 w/ Passenger (Many tickets can be allocated to a single passenger; one passenger may have many tickets)
- M:1 w/ Train (Many tickets may specify a single train; one train may have many tickets)

**Functional dependencies:**

$$
R(\text{passenger\textunderscore id},\text{train\textunderscore id},\text{origin\textunderscore name},\text{origin\textunderscore route},\text{dest\textunderscore name},\text{dest\textunderscore route},\text{departure})\\\
(\text{passenger\textunderscore id},\text{train\textunderscore id}) \rarr (\text{origin\textunderscore name},\text{origin\textunderscore route},\text{dest\textunderscore name},\text{dest\textunderscore route},\text{departure})
$$

**Demonstration of normal form:**
The table is in 3NF because each non-key column is directly tied to the PK, and there are no transitive dependencies.

---

### Table: Passenger

A passenger. May have a ticket for a route.

| Name       | Type   | Description |
| ---------- | ------ | ----------- |
| id         | PRI    |             |
| first_name | String |             |
| last_name  | String |             |

**Cascades:**

- On Deletion: Any associated ticket should be deleted.

**Functional dependencies:**

$$
R(\text{id}, \text{first\textunderscore name}, \text{last\textunderscore name})\\\
(\text{id}) \rarr (\text{first\textunderscore name}, \text{last\textunderscore name})
$$

**Demonstration of normal form:**
The table is in 3NF because each non-key column is directly tied to the PK, and there are no transitive dependencies.

---

## API Design

Each table within the `railway` database has a collection of endpoint reachable at `/api/{table_name}`. In general, each of these tables supports CRUD ops via different request methods @ these endpoints (save for a few).
If an entity has a simple primary key (e.g train, which has a single numerical ID), GET, PUT, and DELETE requests can be specified for a single entity w/ the endpoint `api/{table}/{id}`.
If an entity has a composite primary key (e.g. station, whose key is `route` + `name`), then the same methods can be specified for a single entity with search params: `api/{table}?{param1}={val}&{param2}=val...`

### `/api/links`

#### `GET /api/links`

Returns all links.

Slugs: none
Search params: none

#### `GET /api/links`

Returns a single link by route, origin station, and destination station.

Search params:

- `route: string`
- `origin: string`
- `dest: string`

---

### `/api/passengers`

#### `GET /api/passengers`

Returns all passengers.

---

#### `POST /api/passengers/create`

Creates a new passenger.

Request body:

```json
{
  "first_name": string,
  "last_name": string,
}
```

---

#### `GET /api/passengers/{id}`

Retrieves a single passenger by ID.

Slugs: `id: string`

---

#### `PUT /api/passengers/{id}`

Updates a passenger.

Slugs: `id: string`

Request body:

```json
{
  "first_name": string,
  "last_name": string,
}
```

---

#### `DELETE /api/passengers/{id}`

Deletes a passenger.

Slugs: `id: string`

---

### `/api/schedules`

#### `GET /api/schedules`

Returns all schedules.

---

#### `POST /api/schedules/create`

Creates a new schedule.

Request body:

```json
{
  "id": number,
  "origin": Station,
  "dest": Station,
  "direction": INBOUND | OUTBOUND,
}
```

---

#### `GET /api/schedules/{id}`

Retrieves a single schedule by ID.

Slugs: `id: string`

---

#### `PUT /api/schedules/{id}`

Updates a schedule.

Slugs: `id: string`

Request body:

```json
{
  "id": number,
  "origin": Station,
  "dest": Station,
  "direction": INBOUND | OUTBOUND,
}
```

---

#### `DELETE /api/schedules/{id}`

Deletes a schedule.

Slugs: `id: string`

---

### `/api/stations`

#### `GET /api/stations`

Returns all stations.

---

#### `GET /api/stations`

Returns a single station by route and name.

Search params:

- `route: string`
- `name: string`

---

### `/api/tickets`

#### `GET /api/tickets/`

Returns all tickets.

---

#### `POST /api/tickets/create`

Creates a new ticket.

Request body:

```json
{
  "passenger": Passenger,
  "train": Train,
  "origin": Station,
  "dest": Station,
  "departure": Time,
  "direction": INBOUND | OUTBOUND,
}
```

---

#### `GET /api/tickets`

Retrieves a single ticket by ID.

Search params:

- `train: number`
- `passenger: number`

---

#### `PUT /api/tickets`

Updates a ticket.

Search params:

- `train: number`
- `passenger: number`

Request body:

```json
{
  "passenger": Passenger,
  "train": Train,
  "origin": Station,
  "dest": Station,
  "departure": Time,
  "direction": INBOUND | OUTBOUND,
}
```

---

#### `DELETE /api/tickets`

Deletes a ticket.

Search params:

- `train: number`
- `passenger: number`

---

### `/api/trains`

#### `GET /api/trains`

Returns all trains.

---

#### `POST /api/trains/create`

Creates a new train.

Request body:

```json
{
  "station": Station,
  "link": Link,
  "stationDep": Time,
  "stationArrival": Time,
  "schedule": Schedule,
  "schedDep": Time,
  "status": string,
}
```

---

#### `GET /api/trains/{id}`

Retrieves a single train by ID.

Slugs: `id: string`

---

#### `PUT /api/trains/{id}`

Updates a train.

Slugs: `id: string`

Request body:

```json
{
  "station": Station,
  "link": Link,
  "stationDep": Time,
  "stationArrival": Time,
  "schedule": Schedule,
  "schedDep": Time,
  "status": string,
}
```

---

#### `DELETE /api/trains/{id}`

Deletes a train.

Slugs: `id: string`

---

## Logic Design

### Primary Features

- [x] CRUD ops using front end
- [x] cascades to remove redundant data (e.g. remove ticket if schedule removed)
- [x] timer to automatically update table contents (e.g. moving train from station to station).
- [x] visualization to see trains move

### Common Operations and queries

#### `async getTrainArrivalTime(ticket: Ticket, timestamp: number) => Promise<number>`

Returns the number of milliseconds before the ticket's associated train reaches the platform, or -1 if the train has already departed the platform.

#### `async tick(timestamp: number) => void`

Updates each trains status based on a few rules:

- If the train has no schedule: continue.
- If the train is in transit, and time in transit > link's transit time:
  - move train to next station, update link and arrival time
- If the train is at a station, and time at station >= station's loading time:
  - move train to transit, update departure time.

#### `async getTicketTransitTime(ticket: Ticket, timestamp: number) => Promise<number>`

Gets either the total travelLocalDateTimefor the ticket (from source to destination).

---

## Troubleshooting

### Fails to connect: "Port in use"

Occurs during non-standard shutdown of back end. Requires manual kill of process using port:

- `netstat -ano | findstr 5133`; note PID of task(s) using port
- `taskkill /F /PID <pid>`

# TODO:

- [x] normal form
- [x] functional dependencies
  - [ ] more rigid proof of FDs
- [ ] convenient interface to manage passengers and tickets
- [x] reverse trains when at end of schedule: move to reverse schedule, update direction, and set departure for 10 mins later
- [x] save time in localstorage when leaving page
- [x] update API PUT requests to use JSON body format
- [x] actually use train status
- [x] fix endpoints mapping in front end

```

```
