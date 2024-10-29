CREATE TABLE bank_card(
    id          BIGSERIAL                 PRIMARY KEY,
    user_id     VARCHAR(50)                 NOT NULL,
    number      VARCHAR (30)                NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE	NOT NULL,
    card_type   VARCHAR(50)                 NOT NULL,
    cvc         VARCHAR(10)                 NOT NULL,
    balance     NUMERIC                     NOT NULL,
    priority    boolean                     NOT NULL
)