package chnu.edu.websecurity26.service;

/*
  @author   Artem Petrakovskyi
  @project   web-security26
  @class  BookService
  @version  1.0.0
  @since 08/11/2026 - 15.15
*/

import chnu.edu.websecurity26.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private final List<Book> books = new ArrayList<>();

    {
        books.add(new Book("1", "Kobzar", "Taras Shevchenko", 1840));
        books.add(new Book("2", "Lisova pisnia", "Lesya Ukrainka", 1911));
        books.add(new Book("3", "Zakhar Berkut", "Ivan Franko", 1883));
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public Book getBook(String id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Book createBook(Book book) {
        if (book.getId() == null) {
            book.setId(UUID.randomUUID().toString());
        }
        books.add(book);
        return book;
    }

    public Book updateBook(Book book) {
        Book oldBook = getBook(book.getId());
        if (oldBook == null) {
            return null;
        }
        books.set(books.indexOf(oldBook), book);
        return book;
    }

    public void deleteBook(String id) {
        books.removeIf(book -> book.getId().equals(id));
    }
}
