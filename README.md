# Сервис по рекомендации клиентам банка новых банковских продуктов

## Описание проекта
Учебный проект для формирования рекомендаций по продуктам банка на основе данных пользователей. В проекте используется Spring Boot, базы данных H2.

## Описание окружения
- JDK 17
- Maven
- Spring Boot 3.5.5
- Встраиваемая база данных H2 (файл transaction.mv.db)

## Шаги развертывания
### Установка зависимостей
- Spring Web
- JDBC-драйвер для работы с H2
- Swagger и springdoc-openapi

### Настройка конфигураций (application.properties)
```
spring.application.name=StarProductAdvisor
server.port=8080
```
Подключение H2
```
application.recommendations-db.url=jdbc:h2:file:./transaction
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
- Бурка Максим
- Акинина Анжелика
- Бодунов Артемий
- Роман Митин
