create table room_photo
(
    id        bigint not null auto_increment,
    file_name varchar(255),
    room_id   bigint,
    primary key (id)
);

alter table room_photo
    add constraint FKgawuy9c7kdlpu6ra2qd2k0im7 foreign key (room_id) references room (id);
