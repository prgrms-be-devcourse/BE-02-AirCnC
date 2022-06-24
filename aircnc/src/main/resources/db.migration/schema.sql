drop table if exists review;
drop table if exists wishlist;
drop table if exists room_photo;
drop table if exists trip;
drop table if exists room;
drop table if exists member;
create table member
(
    id           bigint       not null auto_increment,
    email        varchar(50)  not null unique,-- 'RFC 5322'
    password     varchar(255) not null,-- '{Bcrypt} 암호'
    name         varchar(20)  not null,
    birth_date   date         not null,
    phone_number varchar(13)  not null,-- '01x-xxxx-xxxx 형식'
    role         varchar(10)  not null,-- 'GUEST/HOST'
    primary key (id)
);
create table room
(
    id            bigint      not null auto_increment,
    name          varchar(50) not null,
    address       varchar(50) not null,
    description   longtext    not null,
    price_per_day integer     not null,
    capacity      integer     not null,
    review_count  integer     not null,
    host_id       bigint      not null,
    primary key (id)
);
ALTER TABLE `room`
    ADD CONSTRAINT `FK_ROOM_MEMBER` FOREIGN KEY (`host_id`) REFERENCES member (`id`);
CREATE TABLE `review`
(
    `id`         INTEGER      NOT NULL auto_increment,
    `written_at` DATETIME     NOT NULL,
    `content`    VARCHAR(500) NOT NULL,
    `member_id`  bigint      NOT NULL,
    `room_id`    bigint      NOT NULL,
    primary key (id)
);
ALTER TABLE `review`
    ADD CONSTRAINT `FK_REVIEW_MEMBER` FOREIGN KEY (`member_id`) REFERENCES member (`id`);
ALTER TABLE `review`
    ADD CONSTRAINT `FK_REVIEW_ROOM` FOREIGN KEY (`room_id`) REFERENCES room (`id`);
create table room_photo
(
    id        bigint not null auto_increment,
    file_name varchar(200),
    room_id   bigint not null,
    primary key (id)
);
ALTER TABLE `room_photo`
    ADD CONSTRAINT `FK_ROOM_PHOTO_ROOM` FOREIGN KEY (`room_id`) REFERENCES room (`id`);
create table trip
(
    id          bigint      not null auto_increment,
    check_in    date        not null,
    check_out   date        not null,
    total_price integer     not null,
    head_count  integer     not null,
    status      VARCHAR(20) not null,
    room_id     bigint      not null,
    guest_id    bigint      not null,
    primary key (id)
);
ALTER TABLE `trip`
    ADD CONSTRAINT `FK_TRIP_MEMBER` FOREIGN KEY (`guest_id`) REFERENCES member (`id`);
ALTER TABLE `trip`
    ADD CONSTRAINT `FK_TRIP_ROOM` FOREIGN KEY (`room_id`) REFERENCES room (`id`);
CREATE TABLE `wishlist`
(
    `id`        INTEGER     NOT NULL auto_increment,
    `memo`      VARCHAR(50) NOT NULL,
    `member_id` bigint     NOT NULL,
    `room_id`   bigint     NOT NULL,
    primary key (id)
);
ALTER TABLE `wishlist`
    ADD CONSTRAINT `FK_WISHLIST_MEMBER` FOREIGN KEY (`member_id`) REFERENCES member (`id`);
ALTER TABLE `wishlist`
    ADD CONSTRAINT `FK_WISHLIST_ROOM` FOREIGN KEY (`room_id`) REFERENCES room (`id`);