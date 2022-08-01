--liquibase formatted sql
--changeset agnieszka.burkat:create_tables_with_uuid logicalFilePath:path-independent

CREATE TABLE product_uuid
(
    id uuid ,
    title character varying(50)  NOT NULL,
    stock character varying(255) NOT NULL,
    price int                    NOT NULL
);

CREATE TABLE product_category_uuid
(
    id uuid,
    name character varying(50)  NOT NULL
);

--rollback DROP TABLE IF EXISTS excluded_product CASCADE;
--rollback DROP TABLE IF EXISTS product_category CASCADE;