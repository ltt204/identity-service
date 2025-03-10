create table identity_service.invalidated_token (
    token varchar(256) not null primary key,
    expiration_time datetime not null
);