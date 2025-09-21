# Сервис по рекомендации клиентам банка новых банковских продуктов

## Описание проекта
Учебный проект для формирования рекомендаций по продуктам банка на основе данных пользователей. В проекте используется Spring Boot, базы данных H2.

## Описание окружения
- JDK 17
- Maven
- Spring Boot 3.5.5
- База данных системы H2 (с режимом ReadOnly), хранящая данные о пользователях и их транзакциях (файл transaction.mv.db)
- База данных системы PostgreSQL (с режимом Read/Write) для работы с динамическими правилами рекомендаций
- База данных системы PostgreSQL (с режимом Read/Write) для тестирования и замеров производительности репозитория и кеша на реальных данных

## Шаги развертывания
### Установка зависимостей
- Spring Web
- JDBC-драйвер для работы с H2
- Swagger и springdoc-openapi
- Spring Data JPA (API Jakarta Persistence, Hibernate, Spring Data)
- библиотека Liquibase для управления миграциями баз данных
- JDBC-драйвер для PostgreSQL
- библиотеки для тестирования в Spring Boot (JUnit, Mockito, Spring Test и другие)
- стартер Spring Boot для поддержки кеширования
- библиотека Caffeine для высокопроизводительного in-memory кеша
  
### Настройка конфигураций (application.properties)
```
spring.application.name=StarProductAdvisor
server.port=8080
```
Подключение БД системы H2 для хранения статических правил рекомендаций
```
application.recommendations-db.url=jdbc:h2:file:./transaction
```
Подключение БД системы PostgreSQL для хранения динамических правил рекомендаций
```
spring.datasource.url=jdbc:postgresql://localhost:5432/${MY_DB}
spring.datasource.username=${MY_NAME}
spring.datasource.password=${MY_PASS}
spring.datasource.driver-class-name=org.postgresql.Driver
```
Подключение liquibase
```
spring.liquibase.change-log=classpath:/liquibase/changelog-master.yaml
spring.liquibase.enabled=true
```
Настройка Hibernate
```
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false
spring.jpa.show-sql=true
```
### Настройка конфигураций (application-test.properties)
Подключение БД системы PostgreSQL для для тестирования и замеров производительности репозитория и кеша на реальных данных
```
spring.datasource.url=jdbc:postgresql://localhost:5432/Test
spring.datasource.username=test
spring.datasource.password=test
spring.datasource.driver-class-name=org.postgresql.Driver
```
## Запуск приложения
### Сборка
```
mvn clean package
```
### Запуск
```
java -jar target/StarProductAdvisor.jar
```
## Авторы
- Бурка Максим ([byorck](https://github.com/byorck))
- Акинина Анжелика([waffleemilky](https://github.com/waffleemilky))
- Бодунов Артемий([JewRay](https://github.com/JewRay))
- Роман Митин ([Mitrom79](https://github.com/Mitrom79))
