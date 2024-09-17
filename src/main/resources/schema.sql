CREATE TABLE if NOT EXISTS account (
    uuid VARCHAR(1000),
    roles VARCHAR(1000),
    hash VARCHAR(1000),
    salt VARCHAR(1000),
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    PRIMARY KEY (uuid)
);

CREATE TABLE if NOT EXISTS bucket (
    uuid VARCHAR(1000),
    owner VARCHAR(1000),
    name VARCHAR(1000),
    share DOUBLE,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    PRIMARY KEY (uuid),
    FOREIGN KEY (owner) REFERENCES account (uuid)
);

CREATE TABLE if NOT EXISTS income (
    uuid VARCHAR(1000),
    owner VARCHAR(1000),
    amount INTEGER,
    occurred TIMESTAMP NOT NULL,
    category VARCHAR(1000),
    description VARCHAR(1000),
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    PRIMARY KEY (uuid),
    FOREIGN KEY (owner) REFERENCES account (uuid)
);

CREATE TABLE if NOT EXISTS expense (
    uuid VARCHAR(1000),
    owner VARCHAR(1000),
    amount INTEGER,
    occurred TIMESTAMP NOT NULL,
    category VARCHAR(1000),
    description VARCHAR(1000),
    bucket VARCHAR(1000),
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    PRIMARY KEY (uuid),
    FOREIGN KEY (owner) REFERENCES account (uuid)
    FOREIGN KEY (bucket) REFERENCES bucket (uuid)
);

