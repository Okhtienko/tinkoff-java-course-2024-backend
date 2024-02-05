![Bot](https://github.com/Okhtienko/tinkoff-java-course-2024-backend/actions/workflows/bot.yml/badge.svg)
![Scrapper](https://github.com/Okhtienko/tinkoff-java-course-2024-backend/actions/workflows/scrapper.yml/badge.svg)

# Link Tracker

An application for tracking content updates through links. 
When new events occur, a notification is sent to Telegram.

The project is written in `Java 21`  using `Spring Boot 3`.

The project consists of 2 applications:
* Bot
* Scrapper

It requires a `PostgreSQL` database for operation. There is an optional dependency on `Kafka`.
