-- V1: Create the users table
CREATE TABLE lst_users (
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    rsa_id     VARCHAR(13)  NOT NULL UNIQUE,
    version    BIGINT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_lst_users_first_name ON lst_users (first_name);
CREATE INDEX idx_lst_users_last_name  ON lst_users (last_name);
