# web-security26 — Book library REST API

Лабораторні роботи з курсу «Інженерія безпеки веб-додатків».

**Технології:** Java 25, Spring Boot 4.1.1, Spring Security, Maven, Lombok.

Кожна лабораторна лежить в окремій гілці:

| Лабораторна | Гілка |
|---|---|
| 1. Simple web-application | `step-1-web-app` |
| 2. HTTP-requests | `step-2-http-requests` |
| 3. Basic Authorization | `step-3-basic-auth` |

## Лабораторна 1. Simple web-application

Простий REST-застосунок для керування книгами.

- `model/Book.java` — сутність «Книга»: `id`, `title`, `author`, `year`.
- `service/BookService.java` — зберігає книги у списку в пам'яті; на старті там 3 книги з `id` `1`, `2`, `3`.
- `controller/BookController.java` — REST API:

| Метод | Адреса | Що робить |
|---|---|---|
| GET | `/api/v1/books` | усі книги |
| GET | `/api/v1/books/{id}` | одна книга |
| POST | `/api/v1/books` | створити книгу (JSON у тілі) |
| PUT | `/api/v1/books` | оновити книгу (`id` у тілі) |
| DELETE | `/api/v1/books/{id}` | видалити книгу |

- `resources/static/index.html` — стартова сторінка.

**Запуск:** запустити `WebSecurity26Application` і відкрити http://localhost:8080 або http://localhost:8080/api/v1/books.

## Лабораторна 2. HTTP-requests

Файл `src/test/http/crud.http` (у гілці `step-2-http-requests`) — 5 CRUD-запитів для IntelliJ HTTP Client:

1. GET all books — усі книги;
2. GET one book — книга з `id` `1`;
3. CREATE one book — нова книга (`id` генерується автоматично);
4. UPDATE one book — оновлює книгу з `id` `2`;
5. DELETE one book — видаляє книгу з `id` `3`.

**Запуск:** запустити застосунок, відкрити `crud.http` і натиснути **Run All** (подвійна зелена стрілка вгорі файлу).

## Лабораторна 3. Basic Authorization

Усі запити до застосунку захищені Basic-авторизацією.

- `pom.xml` — додано `spring-boot-starter-security` і `spring-security-test`.
- `application.properties` — користувач `admin` з паролем `admin`.
- `src/test/http/basic-authorization.http` — 10 запитів (замість `crud.http`):
  - 1–5: ті самі CRUD-запити з правильним логіном (`Basic admin admin`), очікувана відповідь **200**;
  - 6–10: ті самі запити з неправильним паролем (`Basic admin qwerty`), очікувана відповідь **401 Unauthorized**.
- Кожен запит перевіряє свій статус (`client.test`), тому в панелі Run видно, що всі тести пройшли.

**Чому є запит № 0.** Spring Security за замовчуванням вмикає захист від CSRF. Тому POST, PUT і DELETE без CSRF-токена відхиляються навіть із правильним логіном. Запит № 0 бере токен зі сторінки `/login` і зберігає його у змінну `{{csrf}}`. Запити 3–5 і 8–10 передають його в заголовку `X-CSRF-TOKEN`. Сесійне cookie IntelliJ підставляє сам.
