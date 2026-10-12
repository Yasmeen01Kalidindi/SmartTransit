-- ============================================================
-- SmartTransit - Initial Database Data
-- ============================================================
-- This file inserts the initial bus stops, road connections,
-- and buses used by the SmartTransit project.
--
-- Existing unrelated tables such as "equipment" are NOT touched.
-- ============================================================


-- ============================================================
-- 1. BUS STOPS
-- ============================================================

INSERT INTO bus_stops
    (id, name, location, connected_roads, status, latitude, longitude)
VALUES
    ('ST01', 'SVECW', 'Bhimavaram', 3, 'Active', 16.544, 81.521),

    ('ST02', 'Railway Station', 'Bhimavaram', 4, 'Active', 16.542, 81.523),

    ('ST03', 'Town Hall', 'Bhimavaram', 3, 'Active', 16.540, 81.526),

    ('ST04', 'Market', 'Bhimavaram', 5, 'Active', 16.538, 81.528),

    ('ST05', 'Bhimavaram Bus Stand', 'Bhimavaram', 4, 'Active', 16.537, 81.530),

    ('ST06', 'College Road', 'Bhimavaram', 2, 'Active', 16.545, 81.526)

ON CONFLICT (id) DO NOTHING;


-- ============================================================
-- 2. ROAD CONNECTIONS
-- ============================================================
-- These are the 8 connections from the SmartTransit
-- network-map graph.
--
-- distance     = road distance in km
-- travel_time  = estimated travel time in minutes
-- fare         = estimated fare in rupees
-- traffic_level = LOW / MEDIUM / HIGH
-- blocked      = whether the road is currently blocked
-- ============================================================

INSERT INTO connections
    (id, from_stop, to_stop, distance, travel_time, fare,
     traffic_level, blocked)
VALUES
    ('R01', 'ST01', 'ST02', 2.0, 6.0, 8.0, 'LOW', false),

    ('R02', 'ST01', 'ST06', 1.4, 5.0, 6.0, 'LOW', false),

    ('R03', 'ST02', 'ST03', 1.2, 4.0, 5.0, 'LOW', false),

    ('R04', 'ST06', 'ST03', 1.8, 6.0, 7.0, 'MEDIUM', false),

    ('R05', 'ST03', 'ST04', 1.0, 3.0, 4.0, 'LOW', false),

    ('R06', 'ST04', 'ST05', 1.1, 4.0, 5.0, 'LOW', false),

    ('R07', 'ST03', 'ST05', 2.0, 6.0, 7.0, 'MEDIUM', false),

    ('R08', 'ST02', 'ST04', 1.7, 5.0, 6.0, 'MEDIUM', false)

ON CONFLICT (id) DO NOTHING;


-- ============================================================
-- 3. BUSES
-- ============================================================
-- These match the existing SmartTransit frontend mock data.
-- occupied = currently occupied seats.
-- ============================================================

INSERT INTO buses
    (number, route, capacity, occupied)
VALUES
    ('101',
     'SVECW → Bus Stand',
     45,
     30),

    ('102',
     'Railway Station → Market',
     50,
     39),

    ('103',
     'SVECW → Town Hall',
     40,
     38),

    ('104',
     'Market → Bus Stand',
     45,
     45)

ON CONFLICT (number) DO NOTHING;