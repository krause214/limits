create table if not exists limits (
    id bigserial primary key,
    username varchar(255) unique,
    amount numeric
);

create table if not exists limit_change_operation (
    id bigserial primary key,
    status varchar(255),
    username varchar(255),
	reservation_amount numeric,
    limit_id numeric
);