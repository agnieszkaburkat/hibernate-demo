--liquibase formatted sql
--changeset agnieszka.burkat:create_tables logicalFilePath:path-independent

DROP TABLE IF EXISTS product;
CREATE TABLE product
(
    id    serial                 NOT NULL
        CONSTRAINT product_pk PRIMARY KEY,
    title character varying(50)  NOT NULL,
    stock character varying(255) NOT NULL,
    price int                    NOT NULL
);

DROP TABLE IF EXISTS product_category;
CREATE TABLE product_category
(
    id    serial                 NOT NULL
        CONSTRAINT product_category_pk PRIMARY KEY,
    name character varying(50)  NOT NULL
);

--rollback DROP TABLE IF EXISTS excluded_product CASCADE;
--rollback DROP TABLE IF EXISTS product_category CASCADE;