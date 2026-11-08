package chnu.edu.websecurity26.service;

/*
  @author   Artem Petrakovskyi
  @project   web-security26
  @class  BookService
  @version  1.1.0
  @since 08/11/2026 - 15.15
*/

import chnu.edu.websecurity26.model.Book;
import chnu.edu.websecurity26.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @PostConstruct
    void init() {
        bookRepository.deleteAll();
        bookRepository.saveAll(List.of(
                new Book("1", "Kobzar", "Taras Shevchenko", 1840),
                new Book("2", "Lisova pisnia", "Lesya Ukrainka", 1911),
                new Book("3", "Zakhar Berkut", "Ivan Franko", 1883)
        ));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBook(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Book book) {
        if (book.getId() == null) {
            return null;
        }
        Book existing = bookRepository.findById(book.getId()).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setYear(book.getYear());
        return bookRepository.save(existing);
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}
