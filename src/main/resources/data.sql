INSERT INTO Passenger (firstName, lastName) VALUES
	("John", "Adams"),
  ("Yusra", "Marshall"),
  ("Marnie", "Wilson"),
  ("Myah", "Underwood"),
  ("Evelyn", "Graves"),
  ("Adnan", "Haney"),
  ("Erika", "Nash"),
  ("Raymond", "Walton"),
  ("Carmen", "Little"),
  ("Declan", "Green"),
  ("Cian", "Vaughn");

INSERT INTO Station (name, trainRoute, inboundLink, outboundLink, loadingTime) VALUES
("Waterfront", "Line 1", null, null, 4), -- line 1
("City Hall", "Line 1", null, null, 4),
("Market Cross", "Line 1", null, null, 4),  -- interchange duplicate
("Central Park", "Line 1", null, null, 4),
("Union Square", "Line 1", null, null, 4),
("Tech Hub", "Line 1", null, null, 4),  -- interchange duplicate
("Downtown", "Line 1", null, null, 4),
("Harbor Point", "Line 1", null, null, 4),
("Shopping Mall", "Line 1", null, null, 4),  -- interchange duplicate
("Convention Center", "Line 1", null, null, 4),
("Sports Complex", "Line 1", null, null, 4),
("University", "Line 1", null, null, 4),
("Library", "Line 1", null, null, 4),
("Museum", "Line 1", null, null, 4),
("Airport Express", "Line 2", null, null, 4), -- line 2
("Business District", "Line 2", null, null, 4),
("Market Cross", "Line 2", null, null, 4),
("City Hall", "Line 2", null, null, 4),  -- interchange duplicate
("Arts Center", "Line 2", null, null, 4),
("Riverside", "Line 2", null, null, 4),
("Entertainment Hub", "Line 2", null, null, 4),
("Central Station", "Line 2", null, null, 4),  -- interchange duplicate
("Shopping Mall", "Line 2", null, null, 4),
("Harbor Point", "Line 2", null, null, 4),  -- interchange duplicate
("Zoo Park", "Line 2", null, null, 4),
("Ocean View", "Line 2", null, null, 4),
("Beach Terminal", "Line 2", null, null, 4),
("North Station", "Line 3", null, null, 4), -- line 3
("Science Park", "Line 3", null, null, 4),
("Botanical Garden", "Line 3", null, null, 4),
("Exhibition Center", "Line 3", null, null, 4),
("Financial District", "Line 3", null, null, 4),
("Innovation Park", "Line 3", null, null, 4),
("Tech Hub", "Line 3", null, null, 4),  -- interchange duplicate
("Central Station", "Line 3", null, null, 4),
("Entertainment Hub", "Line 3", null, null, 4),  -- interchange duplicate
("Stadium", "Line 3", null, null, 4),
("Olympic Park", "Line 3", null, null, 4),
("Concert Hall", "Line 3", null, null, 4),
("Theater District", "Line 3", null, null, 4),
("Media Center", "Line 3", null, null, 4),
("Grand Hotel", "Line 3", null, null, 4),
("Conference Center", "Line 3", null, null, 4),
("Food Court", "Line 3", null, null, 4),
("Shopping Plaza", "Line 3", null, null, 4),
("West Terminal", "Line 4", null, null, 4), -- line 4
("Industrial Park", "Line 4", null, null, 4),
("Tech Hub", "Line 4", null, null, 4),
("Innovation Park", "Line 4", null, null, 4),  -- interchange duplicate
("Medical Center", "Line 4", null, null, 4),
("Union Square", "Line 4", null, null, 4),
("Downtown", "Line 4", null, null, 4),  -- interchange duplicate
("East Station", "Line 4", null, null, 4),
("Residential Area", "Line 4", null, null, 4),
("School District", "Line 4", null, null, 4),
("Park & Ride", "Line 4", null, null, 4);

INSERT INTO Link (origin, terminus, duration, distance) VALUES
("Waterfront", "City Hall", 8, 8), -- line 1
("City Hall", "Market Cross", 10, 10),
("Market Cross", "Central Park", 7, 7),
("Central Park", "Union Square", 12, 12),
("Union Square", "Tech Hub", 9, 9),
("Tech Hub", "Downtown", 8, 8),
("Downtown", "Harbor Point", 11, 11),
("Harbor Point", "Shopping Mall", 10, 10),
("Shopping Mall", "Convention Center", 7, 7),
("Convention Center", "Sports Complex", 9, 9),
("Sports Complex", "University", 11, 11),
("University", "Library", 8, 8),
("Library", "Museum", 10, 10),
("Airport Express", "Business District", 9, 9), -- line 2
("Business District", "Market Cross", 11, 11),
("Market Cross", "City Hall", 8, 8),
("City Hall", "Arts Center", 10, 10),
("Arts Center", "Riverside", 7, 7),
("Riverside", "Entertainment Hub", 12, 12),
("Entertainment Hub", "Central Station", 9, 9),
("Central Station", "Shopping Mall", 8, 8),
("Shopping Mall", "Harbor Point", 11, 11),
("Harbor Point", "Zoo Park", 10, 10),
("Zoo Park", "Ocean View", 7, 7),
("Ocean View", "Beach Terminal", 9, 9),
("North Station", "Science Park", 8, 8), -- line 3
("Science Park", "Botanical Garden", 11, 11),
("Botanical Garden", "Exhibition Center", 9, 9),
("Exhibition Center", "Financial District", 10, 10),
("Financial District", "Innovation Park", 7, 7),
("Innovation Park", "Tech Hub", 12, 12),
("Tech Hub", "Central Station", 8, 8),
("Central Station", "Entertainment Hub", 9, 9),
("Entertainment Hub", "Stadium", 11, 11),
("Stadium", "Olympic Park", 10, 10),
("Olympic Park", "Concert Hall", 8, 8),
("Concert Hall", "Theater District", 7, 7),
("Theater District", "Media Center", 9, 9),
("Media Center", "Grand Hotel", 11, 11),
("Grand Hotel", "Conference Center", 8, 8),
("Conference Center", "Food Court", 10, 10),
("Food Court", "Shopping Plaza", 7, 7),
("West Terminal", "Industrial Park", 9, 9), -- line 4
("Industrial Park", "Tech Hub", 11, 11),
("Tech Hub", "Innovation Park", 8, 8),
("Innovation Park", "Medical Center", 10, 10),
("Medical Center", "Union Square", 7, 7),
("Union Square", "Downtown", 12, 12),
("Downtown", "East Station", 9, 9),
("East Station", "Residential Area", 8, 8),
("Residential Area", "School District", 11, 11),
("School District", "Park & Ride", 10, 10);

-- INSERT INTO Train (trainRoute, station, trainStatus) VALUES
--   ("Line 1", "City Hall", "BOARDING");

-- INSERT INTO Ticket (passenger, train, source, dest, departure, direction) VALUES
--   (1, 1, )
