/*
CREATE OR REPLACE FUNCTION lookup_letter(text) RETURNS SETOF text AS $$
BEGIN
RETURN QUERY EXECUTE '
	EXPLAIN SELECT letter 
	FROM sample
	WHERE letter = ''' || $1 || '''';
END
$$ LANGUAGE plpgsql;
*/

CREATE TEMPORARY TABLE sample (letter, junk) AS
	SELECT substring(relname, 1, 1), repeat('x', 250)
	FROM pg_class
	ORDER BY random();  -- add rows in random order

CREATE INDEX i_sample on sample (letter);

WITH letters (letter, count) AS (
	SELECT  letter, COUNT(*)
	FROM sample
	GROUP BY 1
)
SELECT letter, count, (count * 100.0 / (SUM(count) OVER ()))::numeric(4,1) AS "%"
FROM letters
ORDER BY 2 DESC;

ANALYZE sample;

SET enable_seqscan = false;
SET enable_bitmapscan = false;


EXPLAIN SELECT letter 
FROM sample
WHERE letter = 'k'
ORDER BY letter
--WHERE letter = 'p';
--WHERE letter = 'd';

WITH letter (letter, count) AS (
	SELECT letter, COUNT(*)
	FROM sample
	GROUP BY 1
)
SELECT letter AS l, count, lookup_letter(letter)
FROM letter
ORDER BY 2 DESC;

create table test (a int primary key, b int unique, c int);
insert into test values (1,1,1), (2,2,2), (3,3,3), (4,4,4), (5,5,5);

explain select * from test where a = 4 or b = 3;

RESET ALL;

CREATE TEMPORARY TABLE sample1 (id, junk) AS
	SELECT oid, repeat('x', 250)
	FROM pg_proc
	ORDER BY random();  -- add rows in random order

CREATE TEMPORARY TABLE sample2 (id, junk) AS
	SELECT oid, repeat('x', 250)
	FROM pg_class
	ORDER BY random();

SELECT *
FROM sample2 s INNER JOIN sample1 s1 ON s.id = s1.id
ORDER BY s.id
	
EXPLAIN SELECT sample2.junk
FROM sample1 JOIN sample2 ON (sample1.id = sample2.id)
WHERE sample1.id = 33;

EXPLAIN SELECT sample1.junk
FROM sample1 JOIN sample2 ON (sample1.id = sample2.id)
WHERE sample2.id > 33;

EXPLAIN SELECT sample1.junk
FROM sample1 JOIN sample2 ON (sample1.id = sample2.id);

ANALYZE sample1;
ANALYZE sample2;
