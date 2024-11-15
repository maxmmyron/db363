# CMPSC 363 Final Project

## Stack

- mysql (db)
- java spring (backend)
- svelte (front end)

## Requirements

- mysql server running on 3306

## General Setup steps
1. mysql db runs on port 3306 (this app establishes conn. w/ user@localhost, pass: password)
2. connect this app to mysql, which then runs on port 5133
3. front end communnicates with api via http://localhost:5133/api endpoint

## Project info

- `./src/main/resources/init.sql`: mysql setup file (ran on first run to init tables n whatnot)

## Troubleshooting

### Fails to connect: "Port in use" (dep. bc docker no longer used in project)
- `netstat -ano | findstr 5133`; note PID of task(s) using port
- `taskkill /F /PID <pid>`
