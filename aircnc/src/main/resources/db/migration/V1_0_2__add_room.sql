create table room
(
    id               bigint  not null auto_increment,
    detailed_address varchar(255),
    lot_address      varchar(255),
    post_code        varchar(255),
    road_address     varchar(255),
    capacity         integer not null,
    description      longtext,
    name             varchar(255),
    price_per_day    integer not null,
    review_count     integer not null,
    host_id          bigint  not null,
    primary key (id)
);

alter table room
    add constraint FKcfmrj5t5xcmulssuvafuw0jd6 foreign key (host_id) references member (id);
