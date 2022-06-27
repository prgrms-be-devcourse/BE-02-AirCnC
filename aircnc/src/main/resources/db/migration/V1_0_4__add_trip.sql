create table trip
(
    id          bigint  not null auto_increment,
    check_in    date,
    check_out   date,
    head_count  integer not null,
    status      integer,
    total_price integer not null,
    guest_id    bigint,
    room_id     bigint,
    primary key (id)
);

alter table trip
    add constraint FKr6w7slvsykkme2tsn97roekuh foreign key (guest_id) references member (id);

alter table trip
    add constraint FKiimhtqg3c891bdeugnkxc3bw9 foreign key (room_id) references room (id);
