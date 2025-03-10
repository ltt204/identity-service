CREATE TABLE user
(
    id            VARCHAR(255) NOT NULL,
    username      VARCHAR(255) NULL,
    password      VARCHAR(255) NULL,
    first_name    VARCHAR(255) NULL,
    last_name     VARCHAR(255) NULL,
    date_of_birth date NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    user_id  VARCHAR(255) NOT NULL,
    roles_id INT          NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, roles_id)
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (roles_id) REFERENCES `role` (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES user (id);