--liquibase formatted sql
--changeset agnieszka.burkat:create_tables_with_uuid logicalFilePath:path-independent
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
SELECT uuid_generate_v4();

CREATE TABLE product_uuid
(
    id uuid DEFAULT uuid_generate_v4(),
    title character varying(50)  NOT NULL,
    stock character varying(255) NOT NULL,
    price int                    NOT NULL
);

CREATE TABLE product_category_uuid
(
    id uuid DEFAULT uuid_generate_v4(),
    name character varying(50)  NOT NULL
);

--rollback DROP TABLE IF EXISTS excluded_product CASCADE;
--rollback DROP TABLE IF EXISTS product_category CASCADE;