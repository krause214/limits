create table if not exists users (
    id bigserial primary key,
    username varchar(255) unique
);

create table if not exists limits (
    id BIGINT NOT NULL,
    amount numeric,
	PRIMARY KEY (id),
        FOREIGN KEY (id) REFERENCES users(id)
            ON DELETE CASCADE
);

create table if not exists limit_change_operation (
    id bigserial primary key,
    status varchar(255),
    username varchar(255),
	reservation_amount numeric,
    limit_id numeric
);