CREATE TABLE matrix (
	matrix_id int4 NOT NULL,
	i int4 NOT NULL,
	j int4 NOT NULL,
	val float4,
	CONSTRAINT matrix_pk PRIMARY KEY (matrix_id, i, j)
)


INSERT INTO matrix
VALUES (1, 1, 1, 1), (1,2,2,2), (2,1,1,1), (2,1,2,1), (2,2,1,2), (2,2,2,2), (2,3,1,3), (2,3,2,3)

CREATE FUNCTION public.matrix_multiply(integer, integer) RETURNS TABLE(i int4, j int4, val_ float4) AS '
	SELECT m1.i, m2.j, SUM(COALESCE(m1.val,0)*COALESCE(m2.val,0))
	FROM matrix m1, matrix m2
	WHERE m1.j = m2.i
	AND m1.matrix_id = $1 
	AND m2.matrix_id = $2
	GROUP BY 1,2;
' LANGUAGE sql;

DROP FUNCTION public.matrix_multiply(integer, integer)

CREATE FUNCTION public.matrix_multiply(integer, integer) RETURNS TABLE(i int4, j int4, val_ float4) AS $$
BEGIN
	RETURN QUERY SELECT m1.i, m2.j, SUM(COALESCE(m1.val,0)*COALESCE(m2.val,0))
	FROM matrix m1, matrix m2
	WHERE m1.j = m2.i
	AND m1.matrix_id = $1 
	AND m2.matrix_id = $2
	GROUP BY 1,2;
END
$$ LANGUAGE plpgsql;

SELECT * FROM matrix_multiply(1,2)