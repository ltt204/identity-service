CREATE TABLE jpaUser
(
    id            VARCHAR(255) NOT NULL,
    username      VARCHAR(255) NULL,
    password      VARCHAR(255) NULL,
    first_name    VARCHAR(255) NULL,
    last_name     VARCHAR(255) NULL,
    date_of_birth date         NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);
CREATE TABLE `role`
(
    id            INT          NOT NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

ALTER TABLE `role`
    ADD CONSTRAINT uc_role_name UNIQUE (name);


CREATE TABLE jpaPermission
(
    id            INT          NOT NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_permission PRIMARY KEY (id)
);

ALTER TABLE jpaPermission
    ADD CONSTRAINT uc_permission_name UNIQUE (name);

CREATE TABLE user_roles
(
    user_id  VARCHAR(255) NOT NULL,
    roles_id INT          NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, roles_id)
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (roles_id) REFERENCES `role` (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES jpaUser (id);

CREATE TABLE role_permissions
(
    role_id        INT NOT NULL,
    permissions_id INT NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (role_id, permissions_id)
);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permissions_id) REFERENCES jpaPermission (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (role_id) REFERENCES `role` (id);