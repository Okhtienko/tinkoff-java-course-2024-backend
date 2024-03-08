CREATE TABLE IF NOT EXISTS chats
(
    id         BIGSERIAL  NOT NULL PRIMARY KEY,
    chat_id    BIGINT     NOT NULL,
    name       TEXT       NOT NULL,
    created_by TEXT       NOT NULL,

    UNIQUE(chat_id)
);
