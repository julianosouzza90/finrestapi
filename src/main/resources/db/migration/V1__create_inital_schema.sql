CREATE TABLE users
(
    id         BINARY(16) NOT NULL,
    name       VARCHAR(100) NOT NULL,
    lastname   VARCHAR(100) NOT NULL,
    login      VARCHAR(200) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (id)
);

CREATE TABLE wallets
(
    id         BINARY(16) NOT NULL,
    name       VARCHAR(200) NOT NULL,
    user_id    BINARY(16) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (id),
    foreign key wallets(user_id) REFERENCES users(id)
);

CREATE TABLE categories
(
    id         BINARY(16) NOT NULL UNIQUE,
    name       VARCHAR(200) NOT NULL,
    user_id    BINARY(16) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (id),
    FOREIGN KEY categories(user_id) REFERENCES users(id)
);

CREATE TABLE invoices
(
    id           BINARY(16) NOT NULL UNIQUE,
    user_id      BINARY(16) NOT NULL,
    wallet_id    BINARY(16) NOT NULL,
    category_id  BINARY(16) NOT NULL,
    name         VARCHAR(200)   NOT NULL,
    invoice_type VARCHAR(100)   NOT NULL,
    value        DECIMAL(10, 2) NOT NULL,
    due_date     TIMESTAMP      NOT NULL,
    status VARCHAR (100) DEFAULT "PENDING",
    created_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (wallet_id) REFERENCES wallets (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)

);