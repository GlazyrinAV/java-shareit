DROP TABLE IF EXISTS USERS CASCADE;

create table if not exists public.users
(
    id    integer
        primary key GENERATED ALWAYS AS IDENTITY,
    name  varchar(255) not null,
    email varchar(255) not null
        unique
);

DROP TABLE IF EXISTS REQUEST CASCADE;

create table if not exists public.request
(
    id          integer
        primary key GENERATED ALWAYS AS IDENTITY,
    owner_id    integer      not null
        constraint request_users_id_fk
            references public.users,
    description varchar(255) not null,
    created     timestamp    not null
);

DROP TABLE IF EXISTS ITEM CASCADE;

create table if not exists public.item
(
    id          integer
        primary key GENERATED ALWAYS AS IDENTITY,
    name        varchar(255) not null,
    description varchar(255) not null,
    available   boolean      not null,
    owner_id    integer      not null
        constraint item_users_id_fk
            references public.users,
    request_id  integer
        constraint item_request_id_fk
            references public.request
);

DROP TABLE IF EXISTS BOOKING;

create table if not exists public.booking
(
    id      integer
        primary key GENERATED ALWAYS AS IDENTITY,
    booker  integer      not null
        constraint booking_users_id_fk
            references public.users,
    item_id integer      not null
        constraint booking_item_id_fk
            references public.item,
    status  varchar(255) not null,
    start   timestamp,
    finish  timestamp
);

DROP TABLE IF EXISTS COMMENTS;

create table if not exists public.comments
(
    id        integer
        primary key GENERATED ALWAYS AS IDENTITY,
    item_id   integer      not null
        constraint comments_item_id_fk
            references public.item,
    author_id integer      not null
        constraint comments_users_id_fk
            references public.users,
    text      varchar(255) not null,
    created   timestamp    not null
);

