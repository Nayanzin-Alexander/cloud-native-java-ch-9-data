CREATE TABLE users(
  id BIGSERIAL PRIMARY KEY ,
  uuid UUID NOT NULL,
  first_name VARCHAR(128) NOT NULL,
  last_name VARCHAR (128) NOT NULL
);