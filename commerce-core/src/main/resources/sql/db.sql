create schema test_new_orders;

create table test_new_orders.framework
(
    id       serial primary key,
    item_id BIGINT,
    city_id BIGINT,
    is_completed boolean,
    creation_date TIMESTAMP,
    completion_date TIMESTAMP,
    user_id BIGINT
);

insert into test_new_orders.framework (id, item_id, city_id, is_completed, creation_date, completion_date, user_id)
values (200000, 49, 1, false, '2024-07-20 16:49:31.579', '2024-07-27 00:00:00.000', 3),
       (200001, 50, 2, false, '2024-07-21 16:49:31.579', '2024-07-27 00:00:00.000', 4),
       (200002, 51, 3, false, '2024-07-22 17:49:31.579', '2024-07-27 00:00:00.000', 5),
       (200003, 52, 4, false, '2024-07-23 16:49:31.579', '2024-07-27 00:00:00.000', 5),
       (200004, 53, 5, false, '2024-07-22 17:49:31.579', '2024-07-27 00:00:00.000', 5)
       ;


create schema test_orders;

create table test_orders.framework
(
    id       serial primary key,
    item_id BIGINT,
    city_id BIGINT,
    is_completed boolean,
    creation_date TIMESTAMP,
    completion_date TIMESTAMP,
    user_id BIGINT
);
--
-- insert into test_new_orders.framework (id, item_id, city_id, is_completed, creation_date, completion_date, user_id)
-- values (200000, 49, 1, false, null, null, 3),
--        (200001, 50, 2, false, null, null, 4),
--        (200002, 51, 3, false, null, null, 5)
;