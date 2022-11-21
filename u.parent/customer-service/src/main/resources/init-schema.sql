DROP SCHEMA IF EXISTS customer CASCADE;

CREATE SCHEMA customer;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE customer.customers
(
    id uuid NOT NULL,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    first_name character varying COLLATE pg_catalog."default" NOT NULL,
    last_name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT customers_pkey PRIMARY KEY (id)
);

DROP MATERIALIZED VIEW IF EXISTS customer.vw_order_customer;

CREATE MATERIALIZED VIEW customer.vw_order_customer
TABLESPACE pg_default
AS
 SELECT id,
    username,
    first_name,
    last_name
   FROM customer.customers
WITH DATA;

refresh materialized VIEW customer.vw_order_customer;

DROP function IF EXISTS customer.refresh_vw_order_customer;

CREATE OR replace function customer.refresh_vw_order_customer()
returns trigger
AS '
BEGIN
    refresh materialized VIEW customer.vw_order_customer;
    return null;
END;
'  LANGUAGE plpgsql;

DROP trigger IF EXISTS refresh_vw_order_customer ON customer.customers;

CREATE trigger refresh_vw_order_customer
after INSERT OR UPDATE OR DELETE OR truncate
ON customer.customers FOR each statement
EXECUTE PROCEDURE customer.refresh_vw_order_customer();