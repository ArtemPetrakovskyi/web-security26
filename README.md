# web-security26 — Book library REST API

Лабораторні роботи з курсу «Інженерія безпеки веб-додатків».

**Технології:** Java 25, Spring Boot 4.1.1, Maven, Lombok.

Кожна лабораторна лежить в окремій гілці:

| Лабораторна | Гілка |
|---|---|
| 1. Simple web-application | `step-1-web-app` |
| 2. HTTP-requests | `step-2-http-requests` |

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

Файл `src/test/http/crud.http` — 5 CRUD-запитів для IntelliJ HTTP Client:

1. GET all books — усі книги;
2. GET one book — книга з `id` `1`;
3. CREATE one book — нова книга (`id` генерується автоматично);
4. UPDATE one book — оновлює книгу з `id` `2`;
5. DELETE one book — видаляє книгу з `id` `3`.

**Запуск:** запустити застосунок, відкрити `crud.http` і натиснути **Run All** (подвійна зелена стрілка вгорі файлу).
