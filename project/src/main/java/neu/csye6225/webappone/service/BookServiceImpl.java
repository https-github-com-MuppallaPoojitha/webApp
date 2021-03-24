package neu.csye6225.webappone.service;

import neu.csye6225.webappone.dao.BookDao;
import com.timgroup.statsd.StatsDClient;
import neu.csye6225.webappone.pojo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bookService")
public class BookServiceImpl implements BookService {
    @Autowired
    private BookDao bookDao;
    @Autowired
    private StatsDClient statsd;

    @Override
    public Book save(Book book) {
        long startTime = System.currentTimeMillis();
        Book res = bookDao.save(book);
        statsd.recordExecutionTime("DB Response Time - Save Book", System.currentTimeMillis() - startTime);
        return res;
    }

    @Override
    public Book findById(String id) {
        long startTime = System.currentTimeMillis();
        Book res = bookDao.findById(id);
        statsd.recordExecutionTime("DB Response Time - Find Book By Id", System.currentTimeMillis() - startTime);
        return res;
    }

    @Override
    public Book findByIsbn(String isbn) {
        long startTime = System.currentTimeMillis();
        Book res = bookDao.findByIsbn(isbn);
        statsd.recordExecutionTime("DB Response Time - Find Book By ISBN", System.currentTimeMillis() - startTime);
        return res;
    }

    @Override
    public List<Book> findAll() {
        long startTime = System.currentTimeMillis();
        List<Book> res = bookDao.findAll();
        statsd.recordExecutionTime("DB Response Time - Find All Books", System.currentTimeMillis() - startTime);
        return res;
    }

    @Override
    public void deleteById(String id) {
        long startTime = System.currentTimeMillis();
        statsd.recordExecutionTime("DB Response Time - Delete Book", System.currentTimeMillis() - startTime);
        bookDao.deleteById(id);
    }


}
