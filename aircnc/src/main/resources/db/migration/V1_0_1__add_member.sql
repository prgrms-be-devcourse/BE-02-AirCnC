create table member
(
    id           bigint       not null auto_increment,
    birth_date   date         not null,
    email        varchar(255) not null,
    name         varchar(20)  not null,
    password     varchar(255) not null,
    phone_number varchar(255) not null,
    role         varchar(255) not null,
    primary key (id)
);

alter table member
    add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email);
