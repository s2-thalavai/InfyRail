-- Sample data (INSERT IGNORE keeps restarts idempotent)
INSERT IGNORE INTO route (id, source, destination) VALUES
(101, 'Bangalore', 'Chennai'),
(102, 'Pune', 'Mysore'),
(103, 'Delhi', 'Mumbai');

INSERT IGNORE INTO train (id, train_name, arrival_time, departure_time, fare, route_id) VALUES
(16021, 'Kaveri Express',                   '13:00:00', '13:30:00',  834.45, 101),
(12608, 'Lalbagh SF Express',               '18:35:00', '18:45:00',  980.98, 101),
(12640, 'Brindavan Express',                '07:45:00', '08:00:00', 1010.65, 101),
(19968, 'Palace Queen Humsafar Express',    '16:40:00', '16:50:00', 1915.05, 102),
(12781, 'Mysuru Swarna Jayanti SF Express', '14:00:00', '14:15:00', 1100.00, 102),
(12910, 'Garib Rath Express',               '07:00:00', '07:15:00',  980.86, 103),
(12952, 'Rajdhani Express',                 '13:55:00', '14:10:00',  870.98, 103);
