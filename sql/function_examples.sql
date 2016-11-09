CREATE FUNCTION public.add(integer, integer) RETURNS integer AS $$
BEGIN
	RETURN $1 + $2;
END
$$ LANGUAGE plpgsql;

CREATE FUNCTION public.add(fl_ float, int_ integer) RETURNS integer AS $$
BEGIN
	RETURN fl_ + int_ + 1;
END
$$ LANGUAGE plpgsql;

CREATE FUNCTION public.gen_sequence(int_ integer) RETURNS TABLE (i integer, j integer) AS $$
BEGIN
	RETURN QUERY SELECT int_-1, int_ UNION SELECT int_, int_ UNION SELECT int_+1, int_;
END
$$ LANGUAGE plpgsql;

DROP FUNCTION gen_sequence(integer);

SELECT i, j
FROM gen_sequence(1)

SELECT n1, n2, public.add(n1, n2)
FROM
	(SELECT 1 n1, 2 n2
	UNION SELECT 2,3
) d0



SELECT public.add(1.0, 2)

SELECT n1, n2, public.add(n1, n2)
FROM
	(SELECT 1.0 n1, 2 n2
	UNION SELECT 2.0, 3
) d0


CREATE FUNCTION sales_tax(real) RETURNS real AS $$
DECLARE
    subtotal ALIAS FOR $1;
BEGIN
	
    RETURN subtotal * 0.06;
END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION sum_n_product(int, int, OUT sum int, OUT prod int) AS $$
DECLARE
	x ALIAS FOR $1;
	y ALIAS FOR $2;
	tmp int;
BEGIN
	tmp := x+y;
    sum := tmp;
    prod := x * y;
END;
$$ LANGUAGE plpgsql;

SELECT sum_n_product(1,2)
