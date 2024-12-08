INSERT INTO Passenger (first_name, last_name) VALUES
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

INSERT INTO Station (name, train_route, loading_time) VALUES
  ("Waterfront", "Line 1", 4), -- line 1
  ("City Hall", "Line 1", 4),
  ("Market Cross", "Line 1", 4),  -- interchange duplicate
  ("Central Park", "Line 1", 4),
  ("Union Square", "Line 1", 4),
  ("Tech Hub", "Line 1", 4),  -- interchange duplicate
  ("Downtown", "Line 1", 4),
  ("Harbor Point", "Line 1", 4),
  ("Shopping Mall", "Line 1", 4),  -- interchange duplicate
  ("Convention Center", "Line 1", 4),
  ("Sports Complex", "Line 1", 4),
  ("University", "Line 1", 4),
  ("Library", "Line 1", 4),
  ("Museum", "Line 1", 4),
  ("Airport Express", "Line 2", 4), -- line 2
  ("Business District", "Line 2", 4),
  ("Market Cross", "Line 2", 4),
  ("City Hall", "Line 2", 4),  -- interchange duplicate
  ("Arts Center", "Line 2", 4),
  ("Riverside", "Line 2", 4),
  ("Entertainment Hub", "Line 2", 4),
  ("Central Station", "Line 2", 4),  -- interchange duplicate
  ("Shopping Mall", "Line 2", 4),
  ("Harbor Point", "Line 2", 4),  -- interchange duplicate
  ("Zoo Park", "Line 2", 4),
  ("Ocean View", "Line 2", 4),
  ("Beach Terminal", "Line 2", 4),
  ("North Station", "Line 3", 4), -- line 3
  ("Science Park", "Line 3", 4),
  ("Botanical Garden", "Line 3", 4),
  ("Exhibition Center", "Line 3", 4),
  ("Financial District", "Line 3", 4),
  ("Innovation Park", "Line 3", 4),
  ("Tech Hub", "Line 3", 4),  -- interchange duplicate
  ("Central Station", "Line 3", 4),
  ("Entertainment Hub", "Line 3", 4),  -- interchange duplicate
  ("Stadium", "Line 3", 4),
  ("Olympic Park", "Line 3", 4),
  ("Concert Hall", "Line 3", 4),
  ("Theater District", "Line 3", 4),
  ("Media Center", "Line 3", 4),
  ("Grand Hotel", "Line 3", 4),
  ("Conference Center", "Line 3", 4),
  ("Food Court", "Line 3", 4),
  ("Shopping Plaza", "Line 3", 4),
  ("West Terminal", "Line 4", 4), -- line 4
  ("Industrial Park", "Line 4", 4),
  ("Tech Hub", "Line 4", 4),
  ("Innovation Park", "Line 4", 4),  -- interchange duplicate
  ("Medical Center", "Line 4", 4),
  ("Union Square", "Line 4", 4),
  ("Downtown", "Line 4", 4),  -- interchange duplicate
  ("East Station", "Line 4", 4),
  ("Residential Area", "Line 4", 4),
  ("School District", "Line 4", 4),
  ("Park & Ride", "Line 4", 4);

INSERT INTO Link (origin_name, origin_route, dest_name, dest_route, duration, distance) VALUES
  ("Waterfront", "Line 1", "City Hall", "Line 1", 8, 8), -- line 1
  ("City Hall", "Line 1", "Market Cross", "Line 1", 10, 10),
  ("Market Cross", "Line 1", "Central Park", "Line 1", 7, 7),
  ("Central Park", "Line 1", "Union Square", "Line 1", 12, 12),
  ("Union Square", "Line 1", "Tech Hub", "Line 1", 9, 9),
  ("Tech Hub", "Line 1", "Downtown", "Line 1", 8, 8),
  ("Downtown", "Line 1", "Harbor Point", "Line 1", 11, 11),
  ("Harbor Point", "Line 1", "Shopping Mall", "Line 1", 10, 10),
  ("Shopping Mall", "Line 1", "Convention Center", "Line 1", 7, 7),
  ("Convention Center", "Line 1", "Sports Complex", "Line 1", 9, 9),
  ("Sports Complex", "Line 1", "University", "Line 1", 11, 11),
  ("University", "Line 1", "Library", "Line 1", 8, 8),
  ("Library", "Line 1", "Museum", "Line 1", 10, 10),
  ("Airport Express", "Line 2", "Business District", "Line 2", 9, 9), -- line 2
  ("Business District", "Line 2", "Market Cross", "Line 2", 11, 11),
  ("Market Cross", "Line 2", "City Hall", "Line 2", 8, 8),
  ("City Hall", "Line 2", "Arts Center", "Line 2", 10, 10),
  ("Arts Center", "Line 2", "Riverside", "Line 2", 7, 7),
  ("Riverside", "Line 2", "Entertainment Hub", "Line 2", 12, 12),
  ("Entertainment Hub", "Line 2", "Central Station", "Line 2", 9, 9),
  ("Central Station", "Line 2", "Shopping Mall", "Line 2", 8, 8),
  ("Shopping Mall", "Line 2", "Harbor Point", "Line 2", 11, 11),
  ("Harbor Point", "Line 2", "Zoo Park", "Line 2", 10, 10),
  ("Zoo Park", "Line 2", "Ocean View", "Line 2", 7, 7),
  ("Ocean View", "Line 2", "Beach Terminal", "Line 2", 9, 9),
  ("North Station", "Line 3", "Science Park", "Line 3", 8, 8), -- line 3
  ("Science Park", "Line 3", "Botanical Garden", "Line 3", 11, 11),
  ("Botanical Garden", "Line 3", "Exhibition Center", "Line 3", 9, 9),
  ("Exhibition Center", "Line 3", "Financial District", "Line 3", 10, 10),
  ("Financial District", "Line 3", "Innovation Park", "Line 3", 7, 7),
  ("Innovation Park", "Line 3", "Tech Hub", "Line 3", 12, 12),
  ("Tech Hub", "Line 3", "Central Station", "Line 3", 8, 8),
  ("Central Station", "Line 3", "Entertainment Hub", "Line 3", 9, 9),
  ("Entertainment Hub", "Line 3", "Stadium", "Line 3", 11, 11),
  ("Stadium", "Line 3", "Olympic Park", "Line 3", 10, 10),
  ("Olympic Park", "Line 3", "Concert Hall", "Line 3", 8, 8),
  ("Concert Hall", "Line 3", "Theater District", "Line 3", 7, 7),
  ("Theater District", "Line 3", "Media Center", "Line 3", 9, 9),
  ("Media Center", "Line 3", "Grand Hotel", "Line 3", 11, 11),
  ("Grand Hotel", "Line 3", "Conference Center", "Line 3", 8, 8),
  ("Conference Center", "Line 3", "Food Court", "Line 3", 10, 10),
  ("Food Court", "Line 3", "Shopping Plaza", "Line 3", 7, 7),
  ("West Terminal", "Line 4", "Industrial Park", "Line 4", 9, 9), -- line 4
  ("Industrial Park", "Line 4", "Tech Hub", "Line 4", 11, 11),
  ("Tech Hub", "Line 4", "Innovation Park", "Line 4", 8, 8),
  ("Innovation Park", "Line 4", "Medical Center", "Line 4", 10, 10),
  ("Medical Center", "Line 4", "Union Square", "Line 4", 7, 7),
  ("Union Square", "Line 4", "Downtown", "Line 4", 12, 12),
  ("Downtown", "Line 4", "East Station", "Line 4", 9, 9),
  ("East Station", "Line 4", "Residential Area", "Line 4", 8, 8),
  ("Residential Area", "Line 4", "School District", "Line 4", 11, 11),
  ("School District", "Line 4", "Park & Ride", "Line 4", 10, 10);

INSERT INTO Schedule (id, origin_name, origin_route, dest_name, dest_route, direction) VALUES
  (1, 'Museum', 'Line 1', 'Waterfront', 'Line 1', 'INBOUND'),
  (2, 'Waterfront', 'Line 1', 'Museum', 'Line 1', 'OUTBOUND'),
  (3, 'Beach Terminal', 'Line 2', 'Airport Express', 'Line 2', 'INBOUND'),
  (4, 'Airport Express', 'Line 2', 'Beach Terminal', 'Line 2', 'OUTBOUND'),
  (5, 'Shopping Plaza', 'Line 3', 'North Station', 'Line 3', 'INBOUND'),
  (6, 'North Station', 'Line 3', 'Shopping Plaza', 'Line 3', 'OUTBOUND'),
  (7, 'Park & Ride', 'Line 4', 'West Terminal', 'Line 4', 'INBOUND'),
  (8, 'West Terminal', 'Line 4', 'Park & Ride', 'Line 4', 'OUTBOUND');

INSERT INTO Train (schedule_id, schedule_departure, station_name, station_route, station_arrival, station_departure, link_origin_name, link_origin_route, link_dest_name, link_dest_route, train_status) VALUES
  ((SELECT id FROM Schedule WHERE id = 1), '2024-01-01 08:00:00', 'Museum', 'Line 1', '2024-01-01 08:00:00', NULL, 'Library', 'Line 1', 'Museum', 'Line 1', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 1), '2024-01-01 09:00:00', 'Museum', 'Line 1', '2024-01-01 09:00:00', NULL, 'Library', 'Line 1', 'Museum', 'Line 1', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 2), '2024-01-01 08:00:00', 'Waterfront', 'Line 1', '2024-01-01 08:00:00', NULL, 'Waterfront', 'Line 1', 'City Hall', 'Line 1', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 2), '2024-01-01 09:00:00', 'Waterfront', 'Line 1', '2024-01-01 09:00:00', NULL, 'Waterfront', 'Line 1', 'City Hall', 'Line 1', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 3), '2024-01-01 08:00:00', 'Beach Terminal', 'Line 2', '2024-01-01 08:00:00', NULL, 'Ocean View', 'Line 2', 'Beach Terminal', 'Line 2', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 3), '2024-01-01 09:00:00', 'Beach Terminal', 'Line 2', '2024-01-01 09:00:00', NULL, 'Ocean View', 'Line 2', 'Beach Terminal', 'Line 2', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 4), '2024-01-01 08:00:00', 'Airport Express', 'Line 2', '2024-01-01 08:00:00', NULL, 'Airport Express', 'Line 2', 'Business District', 'Line 2', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 4), '2024-01-01 09:00:00', 'Airport Express', 'Line 2', '2024-01-01 09:00:00', NULL, 'Airport Express', 'Line 2', 'Business District', 'Line 2', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 5), '2024-01-01 08:00:00', 'Shopping Plaza', 'Line 3', '2024-01-01 08:00:00', NULL, 'Food Court', 'Line 3', 'Shopping Plaza', 'Line 3', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 5), '2024-01-01 09:00:00', 'Shopping Plaza', 'Line 3', '2024-01-01 09:00:00', NULL, 'Food Court', 'Line 3', 'Shopping Plaza', 'Line 3', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 6), '2024-01-01 08:00:00', 'North Station', 'Line 3', '2024-01-01 08:00:00', NULL, 'North Station', 'Line 3', 'Science Park', 'Line 3', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 6), '2024-01-01 09:00:00', 'North Station', 'Line 3', '2024-01-01 09:00:00', NULL, 'North Station', 'Line 3', 'Science Park', 'Line 3', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 7), '2024-01-01 08:00:00', 'Park & Ride', 'Line 4', '2024-01-01 08:00:00', NULL, 'School District', 'Line 4', 'Park & Ride', 'Line 4', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 7), '2024-01-01 09:00:00', 'Park & Ride', 'Line 4', '2024-01-01 09:00:00', NULL, 'School District', 'Line 4', 'Park & Ride', 'Line 4', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 8), '2024-01-01 08:00:00', 'West Terminal', 'Line 4', '2024-01-01 08:00:00', NULL, 'West Terminal', 'Line 4', 'Industrial Park', 'Line 4', 'BOARDING'),
  ((SELECT id FROM Schedule WHERE id = 8), '2024-01-01 09:00:00', 'West Terminal', 'Line 4', '2024-01-01 09:00:00', NULL, 'West Terminal', 'Line 4', 'Industrial Park', 'Line 4', 'BOARDING');

-- INSERT INTO Ticket (passenger_id, train_id, departure, origin_name, origin_route, dest_name, dest_route, direction) VALUES
--   (1, 1, )
