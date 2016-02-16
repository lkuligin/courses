/*
CREATE TABLE Titanic (
	PassengerId int, --идентификатор пассажира
	Survived boolean,
	Pclass smallint, --класс проезда
	Name varchar(1000),
	Sex varchar(10),
	Age numeric(4,1),
	SibSp smallint,
	Parch smallint,
	Ticket varchar(1000),
	Fare numeric,
	Cabin varchar(100),
	Embarked varchar(100)
);
*/

SELECT COUNT(*) FROM Titanic;
SELECT DISTINCT Pclass as TicketClass FROM Titanic;
SELECT Age, Name FROM Titanic WHERE Age < 5

SELECT Age, Name, to_char(Age, '999') FROM Titanic WHERE Age < 1
SELECT Age, Name, cast(Age as char) FROM Titanic WHERE Age < 1
SELECT Age, Name, Age::char FROM Titanic WHERE Age < 1

SELECT Fare, Name, Age FROM Titanic WHERE Fare > 100 AND Age < 18
SELECT Age, Name, to_char(Fare,'') FROM Titanic WHERE Fare > 100 AND Age < 18

SELECT Fare, Name, Age, Cabin, Pclass  FROM Titanic WHERE Fare > 100 AND Age < 18

SELECT passengerid, Cabin || ' fare: ' || Fare, Embarked FROM Titanic WHERE cabin <> ''

SELECT NOT 101%20 < 102 OR True
SELECT NOT True OR False

SELECT * FROM titanic WHERE name between 'b' AND 'c'
SELECT * FROM titanic WHERE age <> floor(age)
SELECT * FROM titanic WHERE age IN (27,28,29)

SELECT *
FROM titanic
WHERE lower(name) LIKE '%thomas%'

SELECT '{"100": "' || floor((500-round(fare))/100) || '", "10": "' || 
	floor((500-round(fare)-floor((500-round(fare))/100)*100)/10) || '", "1": "' || 
	500-round(fare)-floor((500-round(fare))/100)*100-floor((500-round(fare)-floor((500-round(fare))/100)*100)/10)*10
	|| '"}'
FROM titanic
WHERE fare < 500
	
SELECT name, name ~ '[a-zA-z]+, (Mrs|Mr|Miss|Master|Dr|Don). [a-zA-Z]+( [a-zA-Z]+)?( \([a-zA-Z ]+\))?'
FROM titanic
WHERE not (name ~ '[a-zA-z]+, (Mrs|Mr|Miss|Master|Dr|Don). [a-zA-Z]+( [a-zA-Z]+)?( \([a-zA-Z ]+\))?')

SELECT COUNT(*)
FROM titanic

SELECT corr(age, fare)
FROM titanic

SELECT substring(t1 from position(' ' in t1)+1) as n, COUNT(*)
FROM
	(SELECT name--, regexp_matches(name,'(Miss\. |Mrs\.[A-Za-z ]*\()([A-Za-z]*)')
	, substring(name from '((Mrs|Miss|Dr)\. [a-zA-Z]+)') t1
	FROM titanic
	WHERE sex = 'female' and name ~ '(Mrs|Miss|Dr)\. [a-zA-Z]+'
) d0
GROUP BY substring(t1 from position(' ' in t1)+1)
ORDER BY 2 desc

SELECT substring(t1 from position(' ' in t1)+1) as n, COUNT(*)
FROM
	(SELECT name--, regexp_matches(name,'(Miss\. |Mrs\.[A-Za-z ]*\()([A-Za-z]*)')
	, substring(name from '((Mrs|Miss|Dr)\. [a-zA-Z]+ [a-zA-Z]+)') t1
	FROM titanic
	WHERE sex = 'female' and name ~ '(Mrs|Miss|Dr)\. [a-zA-Z]+'
) d0
GROUP BY substring(t1 from position(' ' in t1)+1)
ORDER BY 2 desc

SELECT l, COUNT(*)
FROM
	(SELECT regexp_split_to_table(name, E'\s*') l
	FROM titanic
) d0
GROUP BY l
ORDER BY 2 desc
