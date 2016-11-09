--CREATE EXTENSION pageinspect

SELECT * FROM heap_page_items(get_raw_page('titanic', 0));

SELECT  get_raw_page::text
FROM    get_raw_page('titanic', 0);

SELECT relname, relpages, reltuples, oid FROM pg_class WHERE relname = 'titanic_pk';

SELECT * FROM pg_type WHERE oid = 16395

select oid, datname from pg_database;

asdasd