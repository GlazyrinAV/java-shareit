DROP TABLE IF EXISTS PUBLIC."USERS" CASCADE ;

create table if not exists PUBLIC."USERS"
(
    ID    INTEGER generated always as identity
        primary key,
    NAME  CHARACTER VARYING not null,
    EMAIL CHARACTER VARYING not null
        unique
);

drop table if exists PUBLIC."ITEM" CASCADE ;

create table if not exists PUBLIC."ITEM"
(
    ID          INTEGER generated always as identity,
    NAME        CHARACTER VARYING not null,
    DESCRIPTION CHARACTER VARYING not null,
    AVAILABLE   BOOLEAN           not null,
    OWNER_ID    INTEGER           not null,
    constraint "Items_pk"
        primary key (ID),
    constraint ITEMS_USERS_ID_FK
        foreign key (OWNER_ID) references PUBLIC.USERS
);

drop table if exists PUBLIC.BOOKING;

create table if not exists PUBLIC."BOOKING"
(
    ID      INTEGER generated always as identity,
    BOOKER  INTEGER           not null,
    ITEM_ID INTEGER           not null,
    STATUS  CHARACTER VARYING not null,
    START   TIMESTAMP without TIME ZONE,
    FINISH  TIMESTAMP without TIME ZONE
);

Drop table if exists PUBLIC.COMMENTS;

create table if not exists PUBLIC."COMMENTS"
(
    "ID"        INTEGER generated always as identity,
    "ITEM_ID"   INTEGER           not null,
    "AUTHOR_ID" INTEGER           not null,
    "TEXT"      CHARACTER VARYING not null,
    "CREATED"   TIMESTAMP         not null
);