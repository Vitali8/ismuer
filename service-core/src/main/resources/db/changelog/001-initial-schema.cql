create table if not exists measures
(
    meter_id   ascii,
    checked_at timestamp,
    amount     decimal,
    unit       text,
    primary key (meter_id, checked_at)
) with clustering order by (checked_at desc);

create table if not exists meter_users
(
    meter_id ascii primary key,
    user     text
);
