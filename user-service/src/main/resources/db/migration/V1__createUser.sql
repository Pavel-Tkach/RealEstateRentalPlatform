CREATE TABLE "user" (
    id              SERIAL                      PRIMARY KEY,
    firstname       VARCHAR(30)                 NOT NULL,
    lastname        VARCHAR(30)                 NOT NULL,
    email           VARCHAR(30)                 NOT NULL,
    password        VARCHAR(30)                 NOT NULL,
    phone           VARCHAR(30)                 NOT NULL,
    birth_date      TIMESTAMP WITH TIME ZONE    NOT NULL
)
