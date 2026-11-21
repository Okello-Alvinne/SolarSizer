-- V3__seed_locations.sql
-- Seeds major cities across Africa and Europe along with 288 typical-day hourly
-- solar irradiance (GHI W/m2) and ambient temperature (C) rows per location.

INSERT INTO locations (name, country, latitude, longitude, timezone) VALUES
-- ==================== AFRICA (48 Countries) ====================
('Algiers', 'Algeria', 36.75380, 3.05880, 'Africa/Algiers'),
('Luanda', 'Angola', -8.83900, 13.28940, 'Africa/Luanda'),
('Cotonou', 'Benin', 6.37030, 2.39120, 'Africa/Porto-Novo'),
('Gaborone', 'Botswana', -24.62820, 25.92310, 'Africa/Gaborone'),
('Ouagadougou', 'Burkina Faso', 12.37140, -1.51970, 'Africa/Ouagadougou'),
('Bujumbura', 'Burundi', -3.36140, 29.35990, 'Africa/Bujumbura'),
('Praia', 'Cabo Verde', 14.93300, -23.51330, 'Atlantic/Cape_Verde'),
('Yaoundé', 'Cameroon', 3.84800, 11.50210, 'Africa/Douala'),
('Bangui', 'Central African Republic', 4.39470, 18.55820, 'Africa/Bangui'),
('N''Djamena', 'Chad', 12.13480, 15.05570, 'Africa/Ndjamena'),
('Moroni', 'Comoros', -11.71720, 43.24730, 'Indian/Comoro'),
('Brazzaville', 'Congo', -4.26340, 15.24290, 'Africa/Brazzaville'),
('Kinshasa', 'DR Congo', -4.44190, 15.26630, 'Africa/Kinshasa'),
('Abidjan', 'Côte d''Ivoire', 5.36000, -4.00830, 'Africa/Abidjan'),
('Djibouti City', 'Djibouti', 11.57210, 43.14560, 'Africa/Djibouti'),
('Cairo', 'Egypt', 30.04440, 31.23570, 'Africa/Cairo'),
('Malabo', 'Equatorial Guinea', 3.75040, 8.73710, 'Africa/Malabo'),
('Asmara', 'Eritrea', 15.32290, 38.92510, 'Africa/Asmara'),
('Mbabane', 'Eswatini', -26.30540, 31.13670, 'Africa/Mbabane'),
('Addis Ababa', 'Ethiopia', 9.03200, 38.74690, 'Africa/Addis_Ababa'),
('Libreville', 'Gabon', 0.41620, 9.46730, 'Africa/Libreville'),
('Banjul', 'Gambia', 13.45490, -16.57900, 'Africa/Banjul'),
('Accra', 'Ghana', 5.60370, -0.18700, 'Africa/Accra'),
('Conakry', 'Guinea', 9.64120, -13.57840, 'Africa/Conakry'),
('Bissau', 'Guinea-Bissau', 11.88170, -15.61780, 'Africa/Bissau'),
('Nairobi', 'Kenya', -1.29210, 36.82190, 'Africa/Nairobi'),
('Mombasa', 'Kenya', -4.04350, 39.66820, 'Africa/Nairobi'),
('Kisumu', 'Kenya', -0.09170, 34.76800, 'Africa/Nairobi'),
('Maseru', 'Lesotho', -29.31510, 27.48690, 'Africa/Maseru'),
('Monrovia', 'Liberia', 6.31560, -10.80740, 'Africa/Monrovia'),
('Tripoli', 'Libya', 32.88720, 13.19130, 'Africa/Tripoli'),
('Antananarivo', 'Madagascar', -18.87920, 47.50790, 'Indian/Antananarivo'),
('Lilongwe', 'Malawi', -13.96260, 33.77410, 'Africa/Blantyre'),
('Bamako', 'Mali', 12.63920, -8.00290, 'Africa/Bamako'),
('Nouakchott', 'Mauritania', 18.07350, -15.95820, 'Africa/Nouakchott'),
('Port Louis', 'Mauritius', -20.16090, 57.50120, 'Indian/Mauritius'),
('Casablanca', 'Morocco', 33.57310, -7.58980, 'Africa/Casablanca'),
('Maputo', 'Mozambique', -25.96920, 32.57320, 'Africa/Maputo'),
('Windhoek', 'Namibia', -22.56090, 17.06580, 'Africa/Windhoek'),
('Niamey', 'Niger', 13.51160, 2.12540, 'Africa/Niamey'),
('Lagos', 'Nigeria', 6.52440, 3.37920, 'Africa/Lagos'),
('Abuja', 'Nigeria', 9.07650, 7.39860, 'Africa/Lagos'),
('Kigali', 'Rwanda', -1.94410, 30.06190, 'Africa/Kigali'),
('Dakar', 'Senegal', 14.71670, -17.46770, 'Africa/Dakar'),
('Victoria', 'Seychelles', -4.61910, 55.45130, 'Indian/Mahe'),
('Freetown', 'Sierra Leone', 8.46570, -13.23170, 'Africa/Freetown'),
('Mogadishu', 'Somalia', 2.04690, 45.31820, 'Africa/Mogadishu'),
('Johannesburg', 'South Africa', -26.20410, 28.04730, 'Africa/Johannesburg'),
('Cape Town', 'South Africa', -33.92490, 18.42410, 'Africa/Johannesburg'),
('Juba', 'South Sudan', 4.85940, 31.57130, 'Africa/Juba'),
('Khartoum', 'Sudan', 15.50070, 32.55990, 'Africa/Khartoum'),
('Dar es Salaam', 'Tanzania', -6.79240, 39.20830, 'Africa/Dar_es_Salaam'),
('Lomé', 'Togo', 6.13750, 1.21230, 'Africa/Lome'),
('Tunis', 'Tunisia', 36.80650, 10.18150, 'Africa/Tunis'),
('Kampala', 'Uganda', 0.34760, 32.58250, 'Africa/Kampala'),
('Lusaka', 'Zambia', -15.38750, 28.32280, 'Africa/Lusaka'),
('Harare', 'Zimbabwe', -17.82520, 31.03350, 'Africa/Harare'),

-- ==================== EUROPE (40 Countries) ====================
('Tirana', 'Albania', 41.32750, 19.81870, 'Europe/Tirane'),
('Vienna', 'Austria', 48.20820, 16.37380, 'Europe/Vienna'),
('Minsk', 'Belarus', 53.90060, 27.55900, 'Europe/Minsk'),
('Brussels', 'Belgium', 50.85030, 4.35170, 'Europe/Brussels'),
('Sarajevo', 'Bosnia and Herzegovina', 43.85630, 18.41310, 'Europe/Sarajevo'),
('Sofia', 'Bulgaria', 42.69770, 23.32190, 'Europe/Sofia'),
('Zagreb', 'Croatia', 45.81500, 15.98190, 'Europe/Zagreb'),
('Nicosia', 'Cyprus', 35.18560, 33.38230, 'Asia/Nicosia'),
('Prague', 'Czechia', 50.07550, 14.43780, 'Europe/Prague'),
('Copenhagen', 'Denmark', 55.67610, 12.56830, 'Europe/Copenhagen'),
('Tallinn', 'Estonia', 59.43700, 24.75360, 'Europe/Tallinn'),
('Helsinki', 'Finland', 60.16990, 24.93840, 'Europe/Helsinki'),
('Paris', 'France', 48.85660, 2.35220, 'Europe/Paris'),
('Berlin', 'Germany', 52.52000, 13.40500, 'Europe/Berlin'),
('Munich', 'Germany', 48.13510, 11.58200, 'Europe/Berlin'),
('Athens', 'Greece', 37.98380, 23.72750, 'Europe/Athens'),
('Budapest', 'Hungary', 47.49790, 19.04020, 'Europe/Budapest'),
('Reykjavik', 'Iceland', 64.14660, -21.94260, 'Atlantic/Reykjavik'),
('Dublin', 'Ireland', 53.34980, -6.26030, 'Europe/Dublin'),
('Rome', 'Italy', 41.90280, 12.49640, 'Europe/Rome'),
('Milan', 'Italy', 45.46420, 9.19000, 'Europe/Rome'),
('Riga', 'Latvia', 56.94960, 24.10520, 'Europe/Riga'),
('Vilnius', 'Lithuania', 54.68720, 25.27970, 'Europe/Vilnius'),
('Luxembourg City', 'Luxembourg', 49.61160, 6.13190, 'Europe/Luxembourg'),
('Valletta', 'Malta', 35.89890, 14.51460, 'Europe/Malta'),
('Chisinau', 'Moldova', 47.01050, 28.86380, 'Europe/Chisinau'),
('Podgorica', 'Montenegro', 42.43040, 19.25940, 'Europe/Podgorica'),
('Amsterdam', 'Netherlands', 52.36760, 4.90410, 'Europe/Amsterdam'),
('Skopje', 'North Macedonia', 41.99810, 21.42540, 'Europe/Skopje'),
('Oslo', 'Norway', 59.91390, 10.75220, 'Europe/Oslo'),
('Warsaw', 'Poland', 52.22970, 21.01220, 'Europe/Warsaw'),
('Lisbon', 'Portugal', 38.72230, -9.13930, 'Europe/Lisbon'),
('Bucharest', 'Romania', 44.42680, 26.10250, 'Europe/Bucharest'),
('Belgrade', 'Serbia', 44.78660, 20.44890, 'Europe/Belgrade'),
('Bratislava', 'Slovakia', 48.14860, 17.10770, 'Europe/Bratislava'),
('Ljubljana', 'Slovenia', 46.05690, 14.50580, 'Europe/Ljubljana'),
('Madrid', 'Spain', 40.41680, -3.70380, 'Europe/Madrid'),
('Stockholm', 'Sweden', 59.32930, 18.06860, 'Europe/Stockholm'),
('Zurich', 'Switzerland', 47.37690, 8.54170, 'Europe/Zurich'),
('Kyiv', 'Ukraine', 50.45010, 30.52340, 'Europe/Kyiv'),
('London', 'United Kingdom', 51.50720, -0.12760, 'Europe/London');

-- Generate 288 typical-day hourly rows (12 months x 24 hours) for every location
INSERT INTO irradiance_typical_day (location_id, month, hour, ghi_w_m2, temp_c)
SELECT
    l.id AS location_id,
    m::SMALLINT AS month,
    h::SMALLINT AS hour,
    ROUND(
        CASE
            WHEN solar.sin_alpha <= 0 THEN 0.00
            ELSE 1361.0
                 * GREATEST(0.45, LEAST(0.66, 0.62 - 0.0026 * ABS(l.latitude)))
                 * solar.sin_alpha
        END::NUMERIC,
        2
    ) AS ghi_w_m2,
    ROUND(
        (
            (27.5 - 0.34 * ABS(l.latitude))
            + (1.5 + 0.18 * ABS(l.latitude))
              * COS(RADIANS((m - 7) * 30.0))
              * (CASE WHEN l.latitude >= 0 THEN 1.0 ELSE -1.0 END)
            + 4.8 * COS(RADIANS((h - 14) * 15.0))
        )::NUMERIC,
        1
    ) AS temp_c
FROM locations l
    CROSS JOIN generate_series(1, 12) AS m
    CROSS JOIN generate_series(0, 23) AS h
    CROSS JOIN LATERAL (
    SELECT
    SIN(RADIANS(l.latitude)) * SIN(RADIANS(23.44) * SIN(RADIANS((360.0 / 365.0) * (((m - 1) * 30.4 + 15) - 81))))
    + COS(RADIANS(l.latitude)) * COS(RADIANS(23.44) * SIN(RADIANS((360.0 / 365.0) * (((m - 1) * 30.4 + 15) - 81))))
    * COS(RADIANS((h - 11.5) * 15.0)) AS sin_alpha
    ) AS solar
ON CONFLICT (location_id, month, hour) DO NOTHING;