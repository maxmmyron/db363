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

## Project info

- `./src/main/resources/init.sql`: mysql setup file (ran on first run to init tables n whatnot)

## Troubleshooting

### Fails to connect: "Port in use" (dep. bc docker no longer used in project)
- `netstat -ano | findstr 5133`; note PID of task(s) using port
- `taskkill /F /PID <pid>`
