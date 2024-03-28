CREATE TABLE IF NOT EXISTS chats
(
    id         BIGINT  NOT NULL PRIMARY KEY,
    name       TEXT    NOT NULL,
    created_by TEXT    NOT NULL,

    UNIQUE(id)
);
