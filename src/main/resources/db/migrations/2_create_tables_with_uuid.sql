--liquibase formatted sql
--changeset agnieszka.burkat:create_tables_with_uuid logicalFilePath:path-independent
DROP TABLE product_uuid;
CREATE TABLE product_uuid
(
    id    uuid,
    title character varying(50)  NOT NULL,
    stock character varying(255) NOT NULL,
    price int                    NOT NULL
);
DROP TABLE IF EXISTS product_category_uuid;

DROP TABLE IF EXISTS department;
CREATE TABLE department
(
    id   uuid primary key,
    name character varying(50) NOT NULL
);

CREATE TABLE product_category_uuid
(
    id   uuid primary key,
    name character varying(50) NOT NULL,
    department uuid                 ,
    CONSTRAINT fk_departament FOREIGN KEY (department) REFERENCES department (id)
);


--rollback DROP TABLE IF EXISTS excluded_product CASCADE;
--rollback DROP TABLE IF EXISTS product_category CASCADE;