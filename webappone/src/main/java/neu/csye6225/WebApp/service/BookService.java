package neu.csye6225.WebApp.service;

import neu.csye6225.WebApp.model.Book;

import java.util.List;

public interface BookService {
    Book save(Book book);
    Book findById(String id);
    Book findByIsbn(String isbn);
    List<Book> findAll();
    void deleteById(String id);
}
