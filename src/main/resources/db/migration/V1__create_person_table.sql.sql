CREATE SCHEMA IF NOT EXISTS ms_person;

CREATE TABLE IF NOT EXISTS person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS enrollment (
    id BIGSERIAL PRIMARY KEY,
    person_id BIGINT NOT NULL REFERENCES person(id),
    bootcamp_id BIGINT NOT NULL,
    enrollment_date DATE NOT NULL,

    UNIQUE(person_id,bootcamp_id)
);