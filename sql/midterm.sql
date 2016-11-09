/*CREATE TABLE Flights(
	AirportFrom varchar(3) NOT NULL,
	AirportTo varchar(3) NOT NULL,
	DistanceKm numeric(10,1) NOT NULL,
	DurationMin int4 NOT NULL,
	CONSTRAINT flight_pk PRIMARY KEY (AirportFrom, AirportTo))
	
DROP TABLE Flights

DROP TABLE Airports

CREATE TABLE Airports(
	AirportCode varchar(3) NOT NULL,
	City varchar(50) NOT NULL,
	CountryCode varchar(3) NOT NULL,
	CONSTRAINT airport_pk PRIMARY KEY (AirportCode, City, CountryCode))
*/

INSERT INTO Flights
VALUES ('SVO', 'LCY', 1500, 240), ('LCY', 'SVO', 1600, 250), ('DMD', 'LCY', 1400, 245), ('LCY', 'DMD', 1450, 220),
('SVO', 'YKS', 2500, 340)

DELETE FROM Airports;
INSERT INTO Airports
VALUES ('SVO', 'Moscow', 'RUS'), ('LCY', 'London', 'ENG'), ('YKS', 'Yakutsk', 'RUS'), ('JFK', 'New York', 'USA')

SELECT COUNT(DISTINCT airportto)
FROM flights f INNER JOIN airports a
ON f.airportfrom = a.airportcode AND a.countrycode = 'RUS' AND a.city = 'Moscow' 

SELECT am, COUNT(1)
FROM
(SELECT COUNT(1) am
FROM flights 
GROUP BY airportfrom
) d0
GROUP BY 1
ORDER BY 1


DROP FUNCTION check_internetional(varchar(3))

CREATE OR REPLACE FUNCTION public.check_internetional(acode varchar(3))
RETURNS bool
LANGUAGE plpgsql
AS $$
BEGIN
	RETURN (SELECT COUNT(*)
	FROM Flights f 
	INNER JOIN Airports a1
	ON f.AirportTo = a1.AirportCode AND f.AirportFrom = acode 
	WHERE a1.CountryCode != (SELECT CountryCode FROM Airports a WHERE a.AirportCode = acode)) > 0;
END
$$

SELECT d0.c1, d0.c2
FROM
	(SELECT a1.countrycode c1, a2.countrycode c2
	FROM airports a1, airports a2 
	WHERE a1.countrycode < a2.countrycode
	GROUP BY 1, 2
) d0 LEFT JOIN
	(SELECT a1.countrycode c1, a2.countrycode c2
	FROM Flights f INNER JOIN airports a1 ON f.airportfrom = a1.airportcode
	INNER JOIN airports a2 ON f.airportto = a2.airportcode
	WHERE a1.countrycode < a2.countrycode
	GROUP BY 1, 2) d1
ON d0.c1 = d1.c1
WHERE d1.c1 IS NULL


SELECT f.airportfrom, f.airportto, f1.airportto
FROM flights f INNER JOIN flights f1
ON f.airportto = f1.airportfrom
LEFT JOIN flights f2
ON f.airportfrom = f2.airportfrom AND f1.airportto = f2.airportto
WHERE f2.airportfrom IS NULL AND f.airportfrom != f1.airportto

SELECT check_internetional('SVO')