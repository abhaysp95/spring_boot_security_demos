insert into users(username, password, enabled) values(
	'user',
	'$2a$10$cRqfrdolNVFW6sAju0eNEOE0VC29aIyXwfsEsY2Fz2axy3MnH8ZGa',
	true
);

insert into users(username, password, enabled) values(
	'admin',
	'$2a$10$cRqfrdolNVFW6sAju0eNEOE0VC29aIyXwfsEsY2Fz2axy3MnH8ZGa',
	true
);

insert into authorities(username, authority) values(
	'user',
	'ROLE_USER'
);

insert into authorities(username, authority) values(
	'admin',
	'ROLE_ADMIN'
);
