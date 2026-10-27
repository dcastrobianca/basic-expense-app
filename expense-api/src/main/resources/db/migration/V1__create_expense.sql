CREATE TABLE expense (
    id          BIGSERIAL PRIMARY KEY,
    description VARCHAR(255)   NOT NULL,
    amount      NUMERIC(19, 2) NOT NULL,
    currency    VARCHAR(3)     NOT NULL,
    rate        NUMERIC(19, 6) NOT NULL,
    amount_eur  NUMERIC(19, 2) NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL
);
