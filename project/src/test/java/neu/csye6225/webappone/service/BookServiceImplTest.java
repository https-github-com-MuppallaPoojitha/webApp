package neu.csye6225.webappone.service;

import com.timgroup.statsd.StatsDClient;
import neu.csye6225.webappone.dao.BookDao;
import neu.csye6225.webappone.dao.UserDao;
import neu.csye6225.webappone.pojo.Book;
import neu.csye6225.webappone.pojo.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
public class BookServiceImplTest {
    @TestConfiguration
    static class BookServiceImplTestContextConfiguration {
        @Bean
        public BookService bookService() {
            return new BookServiceImpl();
        }
    }

    @Autowired
    private BookService bookService;

    @MockBean
    private BookDao bookDao;
    @MockBean
    private StatsDClient statsd;

    private Book book = new Book("Computer Networks",
            "Andrew S. Tanenbaum", "978-0132126953", "May, 2020");
    List<Book> allBooks = new ArrayList();

    @Before
    public void setUp() {
        allBooks.add(book);
        Mockito.when(bookDao.findById(book.getId())).thenReturn(book);
        Mockito.when(bookDao.save(book)).thenReturn(book);
        Mockito.when(bookDao.findAll()).thenReturn(allBooks);
    }

    @Test
    public void findByIdTest() {
        String author = "Andrew S. Tanenbaum";
        Book found = bookService.findById(book.getId());
        assertThat(found.getAuthor()).isEqualTo(author);
    }

    @Test
    public void saveTest() {
        Book newBook = bookService.save(book);
        assertEquals(book.getAuthor(), newBook.getAuthor());
    }

    @Test
    public void findAllTest() {
        List<Book> allFoundBooks = bookService.findAll();
        assertEquals(allFoundBooks.size(), allBooks.size());
    }
}
