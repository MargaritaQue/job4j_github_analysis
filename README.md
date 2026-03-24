# GitHub Analysis
Spring Boot-приложение для периодического сбора данных о репозиториях и коммитах с GitHub с сохранением в PostgreSQL. 
Используются **Spring Scheduler** (`@Scheduled`) и асинхронная синхронизация коммитов (`@Async`).

## Технологии
- Java 21
- Spring Boot 3.3
- Spring Data JPA, PostgreSQL
- Liquibase
- Lombok
- RestTemplate (GitHub REST API)

## Требования
- JDK 21+
- Maven 3.8+
- PostgreSQL (локально или Docker)
