# java-explore-with-me

Описание

Двухмодульное приложение-микросервис для размещения и поиска событий, в котором функционал различается в зависимости от
роли (публичный, авторизованный пользователь или администратор).

Приложение состоит из основного сервиса, основной БД, сервиса статистики просмотров, БД для статистики. Каждая часть
поднимается в отдельном docker-контейнере.


## Стек:

- Java 11 (Core, Collections, Optional, Stream)
- Spring Boot
- Hibernate
- PostgreSQL
- Maven
- Lombok
- Postman
- Docker

## Спецификация сервиса

- [Спецификация API основного сервиса](./ewm-main-service-spec.json)
- [Спецификация API сервиса статистики](./ewm-stats-service-spec.json)

## Дополнительная функциональность - Комментарии
[Ссылка на пул-реквест](https://github.com/yosychenko/java-explore-with-me/pull/5)

**Admin**
* `[GET] /admin/comments?users={users}&events={events}&commentStates={commentStates}&rangeStart={rangeStart}&rangeEnd={rangeEnd}&from={from}&size={size}` – получить список всех комментариев с пагинацией;
* `[GET] /admin/comments/review?from={from}&size={size}` – получить список всех комментариев для ревью;
* `[DELETE] /admin/comments/{commentId}` – удалить комментарий commentId;
* `[PATCH] /admin/comments/{commentId}` - обновить статус комментария.

**Private**
* `[GET] /users/{userId}/comments?eventId={eventId}&from={from}&size={size}` - получить список всех комментариев пользователя userId к событию eventId с пагинацией;
* `[GET] /users/{userId}/comments` - получить список всех комментариев пользователя;
* `[GET] /users/{userId}/comments/rejected` - получить все отклоненные модератором комментарии;
* `[PATCH] /users/{userId}/comments/rejected/{commentId}` - обновить отклоненный модератором комментарий;
* `[POST] /users/{userId}/comments?eventId={eventId}` – создать новый комментарий к событию eventId пользователем userId;
* `[PATCH] /users/{userId}/comments/{commentId}` – обновить свой комментарий commentId пользователем userId;
* `[DELETE] /users/{userId}/comments/{commentId}` - удалить свой комментарий commentId пользователем userId.

**Public**
* `[GET] /comments?eventId={eventId}&from={from}&size={size}` – получить список всех комментариев к событию eventId с пагинацией;
* `[GET] /comments/{commentId}` – получить комментарий commentId.

## ER-диаграмма основного сервиса

![ewm-service-db-er.png](ewm-service-db-er.png)