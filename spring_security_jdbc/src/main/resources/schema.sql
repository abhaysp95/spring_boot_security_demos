create table user_details(
	username varchar_ignorecase(50) not null primary key,
	password varchar_ignorecase(68),
	enabled boolean not null
);

create table authority_details(
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(68) not null,
	constraint fk_authorities_users foreign key(username) references user_details(username)
);

create unique index ix_auth_username on authority_details (username, authority);
