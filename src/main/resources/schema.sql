-- create tables

create table author
(
    id_author bigint not null
        constraint author_pkey
            primary key,
    name      varchar(255),
    surname   varchar(255)
);
alter table author
    owner to postgres;


create table item
(
    id_item             bigint  not null
        constraint item_pkey
            primary key,
    title               varchar(255),
    type                varchar(255),
    year_of_publication integer not null
);
alter table item
    owner to postgres;


create table client
(
    id_client bigint not null
        constraint client_pkey
            primary key,
    email     varchar(255)
        constraint uk_bfgjs3fem0hmjhvih80158x29
            unique,
    name      varchar(255),
    surname   varchar(255)
);
alter table client
    owner to postgres;


create table loan
(
    id_loan   bigint not null
        constraint loan_pkey
            primary key,
    state     varchar(255),
    start_date date not null,
    id_client bigint
    constraint fk1dxatprk9eq8eyphmxw4qxuso
            references client
            on delete cascade
);
alter table loan
    owner to postgres;


create table author_item_link
(
    id_item   bigint not null
        constraint fkryi11o2566b2mbbxaym7h810i
            references item,
    id_author bigint not null
        constraint fkpoqhp53h4j13yau6yptvi3mpv
            references author
);
alter table author_item_link
    owner to postgres;


create table loan_of_item
(
    id_loan_of_item bigint not null
        constraint loan_of_item_pkey
            primary key,
    end_date        date   not null,
    state           varchar(255),
    id_item         bigint
        constraint fkd92ks7sdl38ov6qsq2dio30mf
            references item,
    id_loan         bigint
        constraint fkljtffdx8kpejsh920t7s09d99
            references loan
);

alter table loan_of_item
    owner to postgres;

