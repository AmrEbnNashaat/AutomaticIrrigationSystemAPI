CREATE DATABASE irrigation;
INSERT INTO plot (id, area, time_slot_id, is_being_watered, current_water_level, is_water_level_low) VALUES (nextval('plot_id_sequence'), 100.0, 0, false, 25, false);
INSERT INTO plot (id, area, time_slot_id, is_being_watered, current_water_level, is_water_level_low) VALUES (nextval('plot_id_sequence'), 230, 0, false, 30, false);
INSERT INTO plot (id, area, time_slot_id, is_being_watered, current_water_level, is_water_level_low) VALUES (nextval('plot_id_sequence'), 150.0, 0, false, 29, false);
INSERT INTO plot (id, area, time_slot_id, is_being_watered, current_water_level, is_water_level_low) VALUES (nextval('plot_id_sequence'), 180.0, 0, false, 35, false);

INSERT INTO time_slot (id, duration, water_amount, plot_id, is_assigned) VALUES (nextval('timeslot_id_sequence'), 50, 40, 0, false);
INSERT INTO time_slot (id, duration, water_amount, plot_id, is_assigned) VALUES (nextval('timeslot_id_sequence'), 70, 25, 0, false);
INSERT INTO time_slot (id, duration, water_amount, plot_id, is_assigned) VALUES (nextval('timeslot_id_sequence'), 100, 50, 0, false);
INSERT INTO time_slot (id, duration, water_amount, plot_id, is_assigned) VALUES (nextval('timeslot_id_sequence'), 120, 24, 0, false);



