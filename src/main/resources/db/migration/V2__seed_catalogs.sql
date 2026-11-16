-- V2__seed_catalogs.sql
-- Seed data for device categories, devices (48 items), hardware catalogs, and grid outage presets

-- 1. Device Categories (6 categories)
INSERT INTO device_categories (name, icon) VALUES
                                               ('Kitchen', 'kitchen'),
                                               ('Lighting', 'lightbulb'),
                                               ('Cooling', 'ac_unit'),
                                               ('Entertainment', 'tv'),
                                               ('Office', 'computer'),
                                               ('Tools', 'build');

-- 2. Devices (48 common household and small-business loads)
-- Kitchen (10 devices)
INSERT INTO devices (category_id, name, typical_watts, surge_multiplier, duty_cycle, default_hours_per_day, source_note, is_custom) VALUES
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Kitchen'), 'Medium Refrigerator (250L)', 150.0, 4.5, 0.35, 24.0, 'Energy Star & compressor nameplate average', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Kitchen'), 'Large Double-Door Fridge', 220.0, 4.5, 0.35, 24.0, 'Manufacturer spec; compressor cycles ~35% of day', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Kitchen'), 'Chest Freezer (200L)', 130.0, 5.0, 0.40, 24.0, 'Standard domestic freezer nameplate rating', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Kitchen'), 'Microwave Oven (1000W)', 1100.0, 1.2, 1.00, 0.5, 'Input wattage ~1100W for 1000W cooking output', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Kitchen'), 'Electric Kettle (1.7L)', 2000.0, 1.0, 1.00, 0.3, 'Resistive heating element; pure unity load', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Kitchen'), 'Toaster (2-Slice)', 900.0, 1.0, 1.00, 0.2, 'Standard 850-950W resistive element', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Kitchen'), 'Blender / Smoothie Maker', 600.0, 2.5, 1.00, 0.2, 'Universal motor startup surge', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Kitchen'), 'Coffee Maker (Drip)', 1000.0, 1.0, 0.80, 0.5, 'Heating plate cycles after brew phase', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Kitchen'), 'Induction Cooktop (Single Burner)', 1800.0, 1.1, 0.75, 1.5, 'Medium-high cooking duty cycle', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Kitchen'), 'Water Dispenser (Hot & Cold)', 550.0, 3.0, 0.20, 24.0, '500W heater + 85W compressor intermittent', FALSE);

-- Lighting (6 devices)
INSERT INTO devices (category_id, name, typical_watts, surge_multiplier, duty_cycle, default_hours_per_day, source_note, is_custom) VALUES
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Lighting'), 'LED Bulb (9W - 60W Eq.)', 9.0, 1.0, 1.00, 6.0, 'Standard A19 LED lamp', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Lighting'), 'LED Bulb (15W - 100W Eq.)', 15.0, 1.0, 1.00, 6.0, 'High-lumen indoor LED lamp', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Lighting'), 'LED Tube Light (4ft / 18W)', 18.0, 1.0, 1.00, 8.0, 'T8 LED batten replacement', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Lighting'), 'Outdoor LED Floodlight (50W)', 50.0, 1.1, 1.00, 11.0, 'Dusk-to-dawn security lighting', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Lighting'), 'LED Strip Lighting (5m)', 24.0, 1.0, 1.00, 5.0, '12V/24V SMD 5050 architectural strip', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Lighting'), 'Fluorescent Tube (36W + Ballast)', 44.0, 1.3, 1.00, 8.0, 'Includes magnetic ballast power loss', FALSE);

-- Cooling (7 devices)
INSERT INTO devices (category_id, name, typical_watts, surge_multiplier, duty_cycle, default_hours_per_day, source_note, is_custom) VALUES
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Cooling'), 'Ceiling Fan (AC Motor)', 70.0, 1.5, 1.00, 10.0, '56-inch 3-blade ceiling fan on high', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Cooling'), 'Ceiling Fan (BLDC Energy Saver)', 32.0, 1.1, 1.00, 10.0, 'Brushless DC motor ceiling fan', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Cooling'), 'Standing / Pedestal Fan (16")', 55.0, 1.4, 1.00, 8.0, 'Standard oscillating floor fan', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Cooling'), 'Split AC 1.0 HP (9,000 BTU Inverter)', 850.0, 1.5, 0.65, 8.0, 'Inverter compressor ramps down after cooling', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Cooling'), 'Split AC 1.5 HP (12,000 BTU Inverter)', 1150.0, 1.5, 0.65, 8.0, 'Typical bedroom/office inverter AC', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Cooling'), 'Split AC 1.5 HP (Non-Inverter)', 1350.0, 3.5, 0.70, 8.0, 'Fixed-speed compressor locked-rotor surge', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Cooling'), 'Evaporative Air Cooler', 180.0, 1.8, 1.00, 8.0, 'Blower fan plus small water circulation pump', FALSE);

-- Entertainment (7 devices)
INSERT INTO devices (category_id, name, typical_watts, surge_multiplier, duty_cycle, default_hours_per_day, source_note, is_custom) VALUES
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Entertainment'), '43" LED Smart TV', 75.0, 1.1, 1.00, 5.0, 'IEC 62087 on-mode average power', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Entertainment'), '55" 4K UHD LED TV', 115.0, 1.1, 1.00, 5.0, 'Standard backlight brightness setting', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Entertainment'), '65" OLED / QLED TV', 165.0, 1.1, 1.00, 5.0, 'HDR/4K active viewing consumption', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Entertainment'), 'Soundbar + Wireless Subwoofer', 60.0, 1.2, 1.00, 4.0, 'Average program volume draw', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Entertainment'), 'Gaming Console (PS5 / Xbox Series X)', 180.0, 1.1, 1.00, 3.0, 'Active 3D gameplay power measurement', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Entertainment'), 'Satellite / Cable Decoder Box', 20.0, 1.0, 1.00, 8.0, 'Set-top receiver active mode', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Entertainment'), 'Home Theater AVR Receiver', 220.0, 1.3, 1.00, 3.0, '5.1 channel amplifier at moderate volume', FALSE);

-- Office (9 devices)
INSERT INTO devices (category_id, name, typical_watts, surge_multiplier, duty_cycle, default_hours_per_day, source_note, is_custom) VALUES
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Office'), 'Laptop Charger (65W Ultrabook)', 45.0, 1.1, 1.00, 8.0, 'Average draw during active use & charging', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Office'), 'Workstation / Gaming Laptop (180W)', 130.0, 1.1, 1.00, 6.0, 'CAD/engineering workload average', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Office'), 'Desktop PC + 24" Monitor (Office)', 175.0, 1.2, 1.00, 8.0, '145W tower + 30W IPS monitor', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Office'), '27" External LED Monitor', 35.0, 1.1, 1.00, 8.0, 'QHD/4K LED display at 70% brightness', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Office'), 'Wi-Fi Router + Fiber ONT Modem', 18.0, 1.0, 1.00, 24.0, 'Continuous 24/7 networking load', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Office'), 'Inkjet All-in-One Printer', 25.0, 1.5, 0.25, 2.0, 'Active printing vs idle standby', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Office'), 'Laser Printer (Monochrome)', 550.0, 2.0, 0.15, 1.0, 'Fuser heater peaks intermittently', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Office'), 'Smartphone / Tablet Charger', 15.0, 1.0, 1.00, 3.0, 'USB-C PD fast charger average', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Office'), 'POS Terminal & Receipt Printer', 40.0, 1.2, 1.00, 10.0, 'Small retail shop checkout counter', FALSE);

-- Tools & Utilities (9 devices)
INSERT INTO devices (category_id, name, typical_watts, surge_multiplier, duty_cycle, default_hours_per_day, source_note, is_custom) VALUES
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Tools'), 'Booster Water Pump (0.5 HP)', 375.0, 4.5, 1.00, 1.0, 'Single-phase induction motor startup surge', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Tools'), 'Submersible Borehole Pump (1.0 HP)', 750.0, 5.0, 1.00, 1.5, 'Deep-well pump high locked-rotor current', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Tools'), 'Washing Machine (Front Load, Cold)', 500.0, 3.0, 0.60, 1.5, 'Drum motor agitation + spin cycle', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Tools'), 'Electric Clothes Iron', 1200.0, 1.0, 0.50, 0.5, 'Thermostat cycles heating element ~50%', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Tools'), 'Hair Dryer / Salon Blow Dryer', 1600.0, 1.1, 1.00, 0.5, 'High heat + blower motor', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Tools'), 'Electric Hair Clipper (Professional)', 15.0, 1.5, 1.00, 4.0, 'Barbershop rotary/pivot clipper', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Tools'), 'Hand Power Drill (650W)', 650.0, 2.5, 0.40, 1.0, 'Intermittent workshop drilling', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Tools'), 'Angle Grinder (4.5" / 850W)', 850.0, 2.8, 0.40, 1.0, 'Workshop cutting/grinding duty', FALSE),
                                                                                                                                        ((SELECT id FROM device_categories WHERE name = 'Tools'), 'CCTV System (8 Cameras + NVR)', 65.0, 1.1, 1.00, 24.0, 'PoE NVR with IR night vision active', FALSE);

-- 3. Solar Panel Catalog
INSERT INTO panel_catalog (brand, model, rated_watts, vmp, voc, imp, isc, temp_coeff_pmax, efficiency_pct, price) VALUES
                                                                                                                      ('JinkoSolar', 'Tiger Neo 430W N-Type', 430.0, 32.58, 39.16, 13.20, 13.95, -0.290, 22.02, 135.00),
                                                                                                                      ('JA Solar', 'DeepBlue 3.0 455W Mono', 455.0, 34.82, 41.80, 13.07, 13.87, -0.350, 20.90, 140.00),
                                                                                                                      ('LONGi', 'Hi-MO 6 550W Monocrystalline', 550.0, 41.95, 49.80, 13.12, 13.98, -0.290, 21.30, 175.00),
                                                                                                                      ('Canadian Solar', 'HiKu6 545W Mono PERC', 545.0, 41.70, 49.60, 13.07, 14.00, -0.340, 21.10, 170.00);

-- 4. Battery Catalog (LFP, AGM, NMC)
INSERT INTO battery_catalog (brand, model, chemistry, nominal_voltage, capacity_kwh, usable_dod_pct, round_trip_eff_pct, cycle_life, annual_fade_pct, price) VALUES
                                                                                                                                                                 ('Pylontech', 'US5000 4.8kWh LiFePO4', 'LFP', 48.00, 4.80, 90.00, 95.00, 6000, 1.50, 1350.00),
                                                                                                                                                                 ('BYD', 'Battery-Box Premium LVS 4.0', 'LFP', 51.20, 4.00, 90.00, 95.00, 6000, 1.50, 1450.00),
                                                                                                                                                                 ('Victron Energy', 'LiFePO4 Smart 12.8V 200Ah', 'LFP', 12.80, 2.56, 80.00, 92.00, 3500, 2.00, 980.00),
                                                                                                                                                                 ('Trojan', 'SAG-12-200 Deep Cycle AGM', 'AGM', 12.00, 2.40, 50.00, 80.00, 1200, 5.00, 460.00),
                                                                                                                                                                 ('LG Energy', 'RESU10H Prime NMC', 'NMC', 48.00, 9.60, 90.00, 94.50, 4000, 2.20, 3400.00);

-- 5. Inverter Catalog (GRID_TIED, OFF_GRID, HYBRID)
INSERT INTO inverter_catalog (brand, model, type, continuous_w, surge_w, efficiency_pct, mppt_v_min, mppt_v_max, battery_voltage, price) VALUES
                                                                                                                                             ('Growatt', 'MIN 5000TL-X Grid-Tied', 'GRID_TIED', 5000, 5500, 98.40, 80.0, 550.0, NULL, 780.00),
                                                                                                                                             ('SMA', 'Sunny Boy 6.0 Grid-Tied', 'GRID_TIED', 6000, 6600, 97.60, 100.0, 550.0, NULL, 1350.00),
                                                                                                                                             ('Victron Energy', 'MultiPlus-II 48/3000/35-32', 'OFF_GRID', 2400, 5500, 95.00, 60.0, 250.0, 48.00, 1150.00),
                                                                                                                                             ('Must', 'PV18-3024 VHM 3kW Off-Grid', 'OFF_GRID', 3000, 6000, 93.00, 30.0, 160.0, 24.00, 420.00),
                                                                                                                                             ('Deye', 'SUN-5K-SG03LP1-EU Hybrid', 'HYBRID', 5000, 10000, 97.60, 150.0, 425.0, 48.00, 1250.00),
                                                                                                                                             ('Deye', 'SUN-8K-SG01LP1-EU Hybrid', 'HYBRID', 8000, 16000, 97.60, 150.0, 425.0, 48.00, 1750.00);

-- 6. Grid Outage Profiles (Hybrid Mode Presets)
INSERT INTO grid_outage_profiles (name, outages_per_month, avg_duration_hours, typical_start_hour, is_preset) VALUES
                                                                                                                  ('Reliable Grid (Rare Outages)', 1, 2.0, 18, TRUE),
                                                                                                                  ('Moderate Outages (Weekly Evening Cuts)', 4, 4.0, 19, TRUE),
                                                                                                                  ('Frequent Outages (Load Shedding)', 12, 6.0, 17, TRUE);