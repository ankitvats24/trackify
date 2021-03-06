create table if not exists device_uptime_tracker
(
   device_id varchar(15) not null,
   device_name varchar(250) not null,
   log_time timestamp not null,
   status boolean not null,
   up_time integer not null,
   down_time integer not null,
   created_date date not null,
   status_last_updated timestamp not null
);
create table if not exists device_status_tracker
(
   device_id varchar(15) not null,
   device_name varchar(250) not null,
   log_time timestamp not null,
   status boolean not null
);