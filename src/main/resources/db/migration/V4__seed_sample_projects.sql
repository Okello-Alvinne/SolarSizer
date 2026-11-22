-- V4__seed_sample_projects.sql
-- Seeds sample projects and project_devices for the first registered user.

DO $$
DECLARE
v_user_id UUID;
    v_nairobi_id BIGINT;
    v_mombasa_id BIGINT;
    v_panel_id BIGINT;
    v_battery_id BIGINT;
    v_outage_id BIGINT;
    v_hybrid_project_id UUID := gen_random_uuid();
    v_ongrid_project_id UUID := gen_random_uuid();
    v_fridge_id BIGINT;
    v_light_id BIGINT;
    v_laptop_id BIGINT;
    v_tv_id BIGINT;
BEGIN
    -- Grab the first registered user in the system
SELECT id INTO v_user_id FROM users ORDER BY created_at ASC LIMIT 1;

-- Only seed if a user exists
IF v_user_id IS NOT NULL THEN
SELECT id INTO v_nairobi_id FROM locations WHERE name = 'Nairobi' LIMIT 1;
SELECT id INTO v_mombasa_id FROM locations WHERE name = 'Mombasa' LIMIT 1;
SELECT id INTO v_panel_id FROM panel_catalog ORDER BY id ASC LIMIT 1;
SELECT id INTO v_battery_id FROM battery_catalog WHERE chemistry = 'LFP' ORDER BY id ASC LIMIT 1;
SELECT id INTO v_outage_id FROM grid_outage_profiles ORDER BY id ASC LIMIT 1;

-- 1. Seed a HYBRID project using the DEVICES load input method
INSERT INTO projects (
    id, user_id, name, system_mode, location_id, load_input_method,
    manual_daily_kwh, autonomy_days, backup_hours_target, outage_profile_id,
    tariff_per_kwh, feed_in_per_kwh, panel_id, battery_id,
    tilt_deg, azimuth_deg, system_losses_pct
) VALUES (
             v_hybrid_project_id, v_user_id, 'Nairobi 3-Bedroom Hybrid Home', 'HYBRID',
             COALESCE(v_nairobi_id, 1), 'DEVICES',
             NULL, 1.5, 8.0, v_outage_id,
             0.210, 0.050, v_panel_id, v_battery_id,
             10.0, 0.0, 14.0
         );

-- Lookup sample appliances from the devices catalog
SELECT id INTO v_fridge_id FROM devices WHERE name ILIKE '%fridge%' OR name ILIKE '%refrigerator%' LIMIT 1;
SELECT id INTO v_light_id FROM devices WHERE name ILIKE '%led%' OR name ILIKE '%bulb%' LIMIT 1;
SELECT id INTO v_laptop_id FROM devices WHERE name ILIKE '%laptop%' LIMIT 1;
SELECT id INTO v_tv_id FROM devices WHERE name ILIKE '%tv%' OR name ILIKE '%television%' LIMIT 1;

IF v_fridge_id IS NOT NULL THEN
            INSERT INTO project_devices (project_id, device_id, quantity, hours_per_day, start_hour, always_on)
            VALUES (v_hybrid_project_id, v_fridge_id, 1, 24.0, 0, TRUE);
END IF;

        IF v_light_id IS NOT NULL THEN
            INSERT INTO project_devices (project_id, device_id, quantity, hours_per_day, start_hour, always_on)
            VALUES (v_hybrid_project_id, v_light_id, 8, 5.0, 18, FALSE);
END IF;

        IF v_laptop_id IS NOT NULL THEN
            INSERT INTO project_devices (project_id, device_id, quantity, hours_per_day, start_hour, always_on)
            VALUES (v_hybrid_project_id, v_laptop_id, 2, 6.0, 9, FALSE);
END IF;

        IF v_tv_id IS NOT NULL THEN
            INSERT INTO project_devices (project_id, device_id, quantity, hours_per_day, start_hour, always_on)
            VALUES (v_hybrid_project_id, v_tv_id, 1, 4.0, 19, FALSE);
END IF;

        -- 2. Seed an ON_GRID project using the MANUAL daily kWh method
INSERT INTO projects (
    id, user_id, name, system_mode, location_id, load_input_method,
    manual_daily_kwh, autonomy_days, backup_hours_target, outage_profile_id,
    tariff_per_kwh, feed_in_per_kwh, panel_id, battery_id,
    tilt_deg, azimuth_deg, system_losses_pct
) VALUES (
             v_ongrid_project_id, v_user_id, 'Mombasa Coastal Office Grid-Tie', 'ON_GRID',
             COALESCE(v_mombasa_id, 1), 'MANUAL',
             28.50, NULL, NULL, NULL,
             0.225, 0.080, v_panel_id, NULL,
             12.0, 0.0, 14.0
         );
END IF;
END $$;