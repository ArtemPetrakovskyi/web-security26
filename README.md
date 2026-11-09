# web-security26 — Book library REST API

Лабораторні роботи з курсу «Інженерія безпеки веб-додатків».

**Технології:** Java 25, Spring Boot 4.1.1, Spring Security, MongoDB, Maven, Lombok.

Кожна лабораторна лежить в окремій гілці:

| Лабораторна | Гілка |
|---|---|
| 1. Simple web-application | `step-1-web-app` |
| 2. HTTP-requests | `step-2-http-requests` |
| 3. Basic Authorization | `step-3-basic-auth` |
| 4. CSRF & Audit | `step-4-csrf-audit` |
| 5. Multi-user | `step-5-multi-user` |

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
- `src/test/http/basic-authorization.http` — 10 запитів (замість `crud.http`; у гілці `step-3-basic-auth`):
  - 1–5: ті самі CRUD-запити з правильним логіном (`Basic admin admin`), очікувана відповідь **200**;
  - 6–10: ті самі запити з неправильним паролем (`Basic admin qwerty`), очікувана відповідь **401 Unauthorized**.
- Кожен запит перевіряє свій статус (`client.test`), тому в панелі Run видно, що всі тести пройшли.

**Чому є запит № 0** (лише в гілці `step-3-basic-auth`). Spring Security за замовчуванням вмикає захист від CSRF. Тому POST, PUT і DELETE без CSRF-токена відхиляються навіть із правильним логіном. Запит № 0 бере токен зі сторінки `/login` і зберігає його у змінну `{{csrf}}`. Запити 3–5 і 8–10 передають його в заголовку `X-CSRF-TOKEN`. Сесійне cookie IntelliJ підставляє сам.

## Лабораторна 4. CSRF & Audit

### 1. CSRF — `config/SecurityConfig.java`

Власна конфігурація Spring Security замість стандартної:

- `.csrf(AbstractHttpConfigurer::disable)` — вимикає захист від CSRF;
- `.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())` — кожен запит лише для залогіненого користувача;
- `.httpBasic(Customizer.withDefaults())` — вхід через заголовок `Authorization: Basic`.

**Чому CSRF вимкнено.** CSRF-атака змушує браузер жертви надіслати запит на сайт, де вона вже залогінена: браузер сам додає її cookie. Захист від CSRF вимагає секретний токен у кожному POST, PUT і DELETE. Наш REST API викликають не браузерні форми, а клієнти (IntelliJ HTTP Client, Postman), які передають логін у кожному запиті. Для такого API документація Spring Security радить вимикати CSRF. У лабораторній 3 через цей захист доводилося брати токен зі сторінки `/login`, тепер він не потрібен: запит № 0 і заголовки `X-CSRF-TOKEN` прибрано з `basic-authorization.http`.

### 2. MongoDB — `repository/BookRepository.java`, `service/BookService.java`

- Книги тепер зберігаються в MongoDB (база `web-security26`, колекція `books`), а не у списку в пам'яті.
- `BookRepository extends MongoRepository<Book, String>` дає готові методи `findAll`, `findById`, `save`, `deleteById`.
- Під час запуску `BookService.init()` очищає колекцію і додає 3 книги.
- `pom.xml` — додано `spring-boot-starter-data-mongodb`; `application.properties` — адреса бази.

### 3. Audit — `model/Auditable.java`, `config/AuditConfig.java`, `config/AuditorAwareImpl.java`

- `Auditable` — базовий клас з полями `createdDate`, `createdBy`, `lastModifiedDate`, `lastModifiedBy`, `version`.
- `Book extends Auditable` — кожна книга має ці поля.
- `@EnableMongoAuditing` в `AuditConfig` вмикає автоматичне заповнення цих полів при кожному збереженні.
- `AuditorAwareImpl` повідомляє, хто змінює дані: ім'я залогіненого користувача (`admin`), а для книг, створених під час запуску, — `system`.

Що видно у відповідях:

| Запит | Поля аудиту у відповіді |
|---|---|
| GET | у кожної книги є дата і автор створення та останньої зміни |
| POST | `createdBy` і `lastModifiedBy` = `admin`, `version` = `0` |
| PUT (книга `2`) | `createdBy` = `system`, `lastModifiedBy` = `admin`, нова `lastModifiedDate`, `version` збільшується на 1 |

### 4. HTTP-запити — `src/test/http/csrf-audit.http`

5 запитів: показати всі, прочитати одну, створити, оновити, видалити. Усі з `Authorization: Basic admin admin` і без CSRF-токена.

### Запуск

1. Запустити MongoDB на `localhost:27017`: MongoDB Community Server або `docker compose up -d` (файл `compose.yaml`).
2. Запустити `WebSecurity26Application`.
3. Відкрити `src/test/http/csrf-audit.http` і натиснути **Run All**.

## Лабораторна 5. Multi-user

### 1. Користувачі — `config/SecurityConfig.java`, метод `userDetailsService()`

Замість одного користувача з `application.properties` тепер три, вони зберігаються в пам'яті (`InMemoryUserDetailsManager`):

| Логін | Пароль | Роль |
|---|---|---|
| `admin` | `admin` | `ADMIN` |
| `user` | `user` | `USER` |
| `guest` | `guest` | `GUEST` |

Префікс `{noop}` у паролі означає, що пароль зберігається без шифрування (лише для навчального прикладу). Рядки `spring.security.user.*` з `application.properties` прибрано.

### 2. Права доступу — `config/SecurityConfig.java`, метод `filterChain()`

Правила перевіряються згори донизу, спрацьовує перше, що підходить:

| Адреса | Хто має доступ |
|---|---|
| `/`, `/index.html` | усі, навіть без логіна |
| `GET /api/v1/books/hello/admin` | лише `ADMIN` |
| `GET /api/v1/books/hello/user` | лише `USER` |
| `GET /api/v1/books/hello/man` | `USER` або `ADMIN` |
| `GET /api/v1/books/hello/unknown` | будь-який залогінений користувач |
| `GET`, `POST`, `PUT /api/v1/books/**` | `USER` або `ADMIN` |
| `DELETE /api/v1/books/**` | лише `ADMIN` |

Коди відповідей: **401** — неправильний логін чи пароль; **403** — користувач залогінений, але його ролі бракує прав.

Ендпоінти `hello/...` додано в `controller/BookController.java`.

Аудит із лабораторної 4 тепер показує, хто саме змінив книгу: після запитів від `user` у полях `createdBy` і `lastModifiedBy` буде `user`, а не `admin`.

### 3. HTTP-запити — `src/test/http/multi-user.http`

15 запитів, кожен перевіряє очікуваний статус:

- 1–7 — привітання для різних ролей (200 або 403);
- 8–13 — книги: `guest` не має доступу (403), `user` читає, створює й оновлює (200), але не видаляє (403), `admin` видаляє (200);
- 14 — неправильний пароль (401);
- 15 — стартова сторінка без логіна (200).

**Запуск:** MongoDB, `WebSecurity26Application`, потім **Run All** у `multi-user.http`.
