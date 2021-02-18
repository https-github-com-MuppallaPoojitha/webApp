package com.cloud.assignment2.Repository;

import com.cloud.assignment2.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book save(Book book);
    Book findById(String id);
    Book findByIsbn(String isbn);
    List<Book> findAll();
    void deleteById(String id);
}