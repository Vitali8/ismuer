create table if not exists measures
(
    meter_id   ascii,
    checked_at timestamp,
    amount     decimal,
    primary key (meter_id, checked_at)
) with clustering order by (checked_at desc);

create table if not exists meters
(
    meter_id       ascii primary key,
    user           text,
    unit           text,
    deactivated_at timestamp,
    added_at       timestamp
);

create table if not exists user_meters
(
    user     text,
    meter_id ascii,
    primary key (user, meter_id)
);
