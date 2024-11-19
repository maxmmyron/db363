CREATE DATABASE IF NOT EXISTS Railway;
USE Railway;

CREATE TABLE IF NOT EXISTS Train (
	id Integer AUTO_INCREMENT PRIMARY KEY,
	train_route INT,
	station INT,
	train_status VARCHAR(255),
	FOREIGN KEY (train_route) REFERENCES TrainRoute(id),
	FOREIGN KEY (station) REFERENCES Station(id),
);

CREATE TABLE IF NOT EXISTS Station (
	ID INT AUTO_INCREMENT PRIMARY KEY,
	station_name VARCHAR(255) UNIQUE,
	train_route INT,
	inbound INT,
	outbound INT,
	loading_time INT,
	FOREIGN KEY (train_route) REFERENCES TrainRoute(id),
	FOREIGN KEY (inbound) REFERENCES Station(id),
	FOREIGN KEY (outbound) REFERENCES Station(id),
);

-- TODO: check if (origin, terminus) is a valid primary key for this table
CREATE TABLE IF NOT EXISTS Link (
	origin INT,
	terminus INT,
	duration INT,
	distance INT,
	PRIMARY KEY (origin, terminus),
	FOREIGN KEY (origin) REFERENCES Station(id),
	FOREIGN KEY (terminus) REFERENCES Station(id),
);

CREATE TABLE IF NOT EXISTS TrainRoute (
	id INT AUTO_INCREMENT PRIMARY KEY,
	source INT,
	dest INT,
	duration INT,
	distance INT,
	FOREIGN KEY (source) REFERENCES Station(id),
	FOREIGN KEY (dest) REFERENCES Station(id),
);

CREATE TABLE IF NOT EXISTS Ticket (
	id INT AUTO_INCREMENT PRIMARY KEY,
	passenger INT,
	train INT,
	source INT,
	dest INT,
	departure DATETIME,
	direction VARCHAR(8),
	FOREIGN KEY (passenger) REFERENCES Passenger(id),
	FOREIGN KEY (train) REFERENCES Train(id),
	FOREIGN KEY (source) REFERENCES Station(id),
	FOREIGN KEY (dest) REFERENCES Station(id),
);

CREATE TABLE IF NOT EXISTS Passenger (
	id INT AUTO_INCREMENT PRIMARY KEY,
	first_name VARCHAR(63),
	last_name VARCHAR(63),
);
