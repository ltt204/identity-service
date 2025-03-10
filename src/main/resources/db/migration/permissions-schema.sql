CREATE TABLE permission
(
    id            INT NOT NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_permission PRIMARY KEY (id)
);

ALTER TABLE permission
    ADD CONSTRAINT uc_permission_name UNIQUE (name);