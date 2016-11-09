/*CREATE TABLE hierarchy
	(employee_id int,
	employee CHARACTER VARYING(25),
	department_id int,
	manager_id int,
	CONSTRAINT hierarchy_pk PRIMARY KEY (employee_id))
	
ALTER TABLE hierarchy ADD COLUMN subordinates int NULL
DROP TABLE hierarchy
*/

TRUNCATE TABLE hierarchy

INSERT INTO hierarchy
VALUES (1, 'Иванов Иван', 1, NULL), (2, 'Петров Петр', 1, 1), (3, 'Сидоров Иван', 1, 2), (4, 'Петрова Марфа', 2, 1),
(5, 'Сидорова Глаша', 2, 4), (6, 'Козлов Василий', 1, 3)

UPDATE hierarchy
SET subordinates = (SELECT COUNT(*) FROM hierarchy h1 WHERE h1.manager_id = hierarchy.employee_id)


SELECT *
FROM hierarchy

WITH RECURSIVE full_hierarchy (manager_id, employee_id, depth)   AS
	(SELECT manager_id, employee_id, 1 depth
	FROM hierarchy
	UNION ALL
	SELECT fh.manager_id, h.employee_id, depth + 1
	FROM hierarchy h, full_hierarchy fh
	WHERE h.manager_id = fh.employee_id AND fh.manager_id IS NOT NULL
)
SELECT * 
FROM full_hierarchy
ORDER BY manager_id, employee_id


CREATE TEMP TABLE ids (
	new_id SERIAL UNIQUE,
	old_id int);
	
INSERT INTO ids (old_id)
SELECT matrix_id
FROM matrix
GROUP BY 1

SELECT matrix_id, range() OVER ()
FROM matrix
GROUP BY 1


DROP TABLE ids; 
SELECT *
FROM ids

INSERT INTO matrix
VALUES (4, 1,1,1)