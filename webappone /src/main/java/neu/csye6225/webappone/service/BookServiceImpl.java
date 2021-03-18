package neu.csye6225.webappone.service;

import neu.csye6225.webappone.dao.BookDao;
import neu.csye6225.webappone.pojo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bookService")
public class BookServiceImpl implements BookService {
    @Autowired
    private BookDao bookDao;

    @Override
    public Book save(Book book) {
        return bookDao.save(book);
    }

    @Override
    public Book findById(String id) {
        return bookDao.findById(id);
    }

    @Override
    public Book findByIsbn(String isbn) { return bookDao.findByIsbn(isbn); };

    @Override
    public List<Book> findAll() { return bookDao.findAll(); }

    @Override
    public void deleteById(String id) { bookDao.deleteById(id);}


}
