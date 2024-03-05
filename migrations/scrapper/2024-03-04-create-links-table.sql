CREATE TABLE IF NOT EXISTS links
(
    id          BIGSERIAL                NOT NULL PRIMARY KEY,
    url         TEXT                     NOT NULL,
    create_by   TEXT                     NOT NULL,
    last_check  TIMESTAMP WITH TIME ZONE NOT NULL,
    create_at   TIMESTAMP WITH TIME ZONE NOT NULL,

    UNIQUE(url)
);
