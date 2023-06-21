DROP TABLE IF EXISTS PUBLIC."User" CASCADE ;

create table if not exists PUBLIC."Users"
(
    ID    INTEGER generated always as identity
        primary key,
    NAME  CHARACTER VARYING not null,
    EMAIL CHARACTER VARYING not null
        unique
);

drop table if exists PUBLIC."Items" CASCADE ;

create table if not exists PUBLIC."Item"
(
    ID          INTEGER generated always as identity,
    NAME        CHARACTER VARYING not null,
    DESCRIPTION CHARACTER VARYING not null,
    AVAILABLE   BOOLEAN           not null,
    OWNER_ID    INTEGER           not null,
    constraint "Items_pk"
        primary key (ID),
    constraint ITEMS_USERS_ID_FK
        foreign key (OWNER_ID) references PUBLIC."Users"
);

drop table if exists PUBLIC."Booking";

create table if not exists PUBLIC."Booking"
(
    ID      INTEGER generated always as identity,
    BOOKER  INTEGER           not null,
    ITEM_ID INTEGER           not null,
    STATUS  CHARACTER VARYING not null,
    START   DATE,
    FINISH  DATE
);


