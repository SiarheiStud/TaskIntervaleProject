CREATE TABLE book(
	id bigserial,
	ISBN varchar(13) not null,
	title varchar(200) not null,
	author varchar(20) not null,
	numberOfPages integer not null,
	weight decimal not null,
	price double precision not null,
	PRIMARY KEY(id)
)
Next_command