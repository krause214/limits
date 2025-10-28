create table if not exists limits (
    id bigserial primary key,
    userId varchar(255) unique,
    amount numeric
);

create table if not exists limit_change_operation (
    id bigserial primary key,
    status varchar(255),
    user_id varchar(255),
	reservation_amount numeric,
    limit_id numeric
);