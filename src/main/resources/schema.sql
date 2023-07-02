DROP TABLE IF EXISTS PUBLIC."User" CASCADE ;

create table if not exists PUBLIC."Users"
(
    ID    INTEGER generated always as identity
        primary key,
    NAME  CHARACTER VARYING not null,
    EMAIL CHARACTER VARYING not null
        unique
);

