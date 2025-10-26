create table if not exists users (
    id bigserial primary key,
    username varchar(255) unique,
    limit_id integer
);

create table if not exists limits (
    id bigserial primary key,
    amount numeric,
	user_id integer not null,
    constraint fk_user foreign key (user_id) references users(id)
);

create table if not exists limit_change_operation (
    id bigserial primary key,
    status varchar(255),
    username varchar(255),
	reservation_amount numeric
);