CREATE TABLE IF NOT EXISTS links_chats
(
    link_id   BIGINT  NOT NULL,
    chat_id   BIGINT  NOT NULL,

    PRIMARY KEY (link_id, chat_id),

    FOREIGN KEY (link_id) REFERENCES links(id) ON UPDATE CASCADE,
    FOREIGN KEY (chat_id) REFERENCES chats(id) ON UPDATE CASCADE
);
