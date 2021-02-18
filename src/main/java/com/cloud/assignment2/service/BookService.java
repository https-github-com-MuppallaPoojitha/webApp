package com.cloud.assignment2.service;
import com.cloud.assignment2.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);
    Book findById(String id);
    Book findByIsbn(String isbn);
    List<Book> findAll();
    void deleteById(String id);
}
