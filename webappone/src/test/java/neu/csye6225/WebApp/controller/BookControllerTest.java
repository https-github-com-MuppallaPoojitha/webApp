package neu.csye6225.WebApp.controller;

import neu.csye6225.WebApp.model.Book;
import neu.csye6225.WebApp.model.User;
import neu.csye6225.WebApp.service.BookService;
import neu.csye6225.WebApp.utils.auth.UserAuthorization;
import neu.csye6225.WebApp.utils.validation.BookRequestBodyValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private UserAuthorization userAuthorization;
    @MockBean
    private BookRequestBodyValidator bookRequestBodyValidator;

    private User user = new User("Jane", "Doe", "janeDoe@example.com", "12345aA!");
    private Book book = new Book("Computer Networks",
            "Andrew S. Tanenbaum", "978-0132126953", "May, 2020");
    HashMap<String, String> authRes = new HashMap<>();
    HashMap<String, String> reqBodyRes = new HashMap<>();
    HashMap<String, String> emptyMap = new HashMap<>();
    List<Book> allBooks = new ArrayList<>();

    @Before
    public void setup() {
        authRes.put("username", user.getUsername());
        authRes.put("status", "200");
        authRes.put("id", user.getId());
        book.setUser_id(user.getId());
        allBooks.add(book);
        given(userAuthorization.check(null)).willReturn(authRes);
        given(bookService.findById(book.getId())).willReturn(book);
        given(bookService.findAll()).willReturn(allBooks);
        given(bookRequestBodyValidator.checkForPost(emptyMap)).willReturn(reqBodyRes);
    }

    @Test
    public void getBookById_valid() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/books/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllBooks_valid() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteBookById_valid() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .delete("/books/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void createBook_valid() throws Exception {
        MockHttpServletRequestBuilder mockHttpReq = MockMvcRequestBuilders
                .post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Computer Networks\", " +
                        "\"author\": \"Andrew S. Tanenbaum\", \"isbn\": \"978-0132126953\", " +
                        "\"published_date\": \"May, 2020\"}");
        this.mvc.perform(mockHttpReq)
                .andExpect(status().isCreated());
    }




}
