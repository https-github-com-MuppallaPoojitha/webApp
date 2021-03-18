package neu.csye6225.webappone.utils.validation;

import neu.csye6225.webappone.pojo.Book;
import neu.csye6225.webappone.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={BookRequestBodyValidator.class})
public class BookRequestBodyValidatorTest {
    @Autowired
    private BookRequestBodyValidator bookRequestBodyValidator;

    @MockBean
    private BookService bookService;

    private Book book = new Book("Computer Networks",
            "Andrew S. Tanenbaum", "978-0132126953", "May, 2020");

    @Before
    public void setup() {
        when(bookService.findByIsbn(book.getIsbn())).thenReturn(null);
    }

    @Test
    public void checkForPostTest() {
        HashMap<String, String> bookInput = new HashMap<>();
        // invalid title
        assertEquals(bookRequestBodyValidator.checkForPost(bookInput).get("error"),
                "Please do not leave the book 'title' empty!");
        bookInput.put("title", book.getTitle());
        // invalid author
        bookInput.put("author", "");
        assertEquals(bookRequestBodyValidator.checkForPost(bookInput).get("error"),
                "Please do not leave the book 'author' empty!");
        bookInput.put("author", book.getAuthor());
        // invalid isbn
        bookInput.put("isbn", "");
        assertEquals(bookRequestBodyValidator.checkForPost(bookInput).get("error"),
                "Please enter a valid 'isbn' in the format of XXX-XXXXXXXXXX.");
        bookInput.put("isbn", book.getIsbn());
        // invalid password
        bookInput.put("published_date", "may, 202");
        assertEquals(bookRequestBodyValidator.checkForPost(bookInput).get("error"),
                "Please enter 'published_date' in the format of 'MMM, YYYY', month in characters only.");
        bookInput.put("published_date", book.getPublished_date());
        // successful post
        assertTrue(bookRequestBodyValidator.checkForPost(bookInput).containsKey("ok"));
    }
}
