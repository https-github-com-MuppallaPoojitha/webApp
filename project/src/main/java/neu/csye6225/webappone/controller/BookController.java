package neu.csye6225.webappone.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import neu.csye6225.webappone.pojo.Book;
import neu.csye6225.webappone.pojo.File;
import neu.csye6225.webappone.service.BookService;
import neu.csye6225.webappone.service.FileService;
import neu.csye6225.webappone.service.S3FileService;
import neu.csye6225.webappone.utils.auth.UserAuthorization;
import neu.csye6225.webappone.utils.validation.BookRequestBodyValidator;
import neu.csye6225.webappone.utils.validation.FileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RestController
@RequestMapping("/mybooks")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private S3FileService s3FileService;
    @Autowired
    private UserAuthorization userAuthorization;
    @Autowired
    private BookRequestBodyValidator bookRequestBodyValidator;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");

    /**
     * This method handles the GET call to /books which gets all books' information.
     */
    @GetMapping(produces = "application/json")
    public @ResponseBody ResponseEntity<?> getAllBooks() {
        List<Book> allBooks = bookService.findAll();
        if (allBooks.isEmpty()) {
            HashMap<String, String> response = new HashMap<>();
            response.put("msg", "There are currently no books.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }

    /**
     * This method handles the GET call to /books/{id} which use book id to get book information.
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> getBookById(@PathVariable String id) {
        // check for book id validity
        Book book = bookService.findById(id);
        HashMap<String, String> errMsg = new HashMap<>();
        if (book == null) {
            errMsg.put("error", "There is no book found with id " + id);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(book.serializeToMap(), HttpStatus.OK);
        }

    }

    /**
     * This method handles the DELETE call to /books which use book id to delete book.
     */
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookById(HttpServletRequest request, @PathVariable String id) throws Exception{
        // check for authorization
        String header = request.getHeader("Authorization");
        HashMap<String, String> authResult = userAuthorization.check(header);
        if (!authResult.get("status").equals("200")) { // if auth is invalid
            return noAuthResponse(authResult);
        }

        // check for book id validity
        Book book = bookService.findById(id);
        HashMap<String, String> errMsg = new HashMap<>();
        if (book == null) {
            errMsg.put("error", "There is no book found with id " + id);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        } else if (!book.getUser_id().equals(authResult.get("id"))) {
            errMsg.put("error", "This book does not belong to you.");
            return new ResponseEntity<>(errMsg, HttpStatus.UNAUTHORIZED);
        } else {
            for (File f : book.getBook_images()) {
                HashMap<String, String> s3ImgDeleteResult = s3FileService.deleteFile(f.getS3_object_name());
                if (!s3ImgDeleteResult.containsKey("ok")) {
                    return new ResponseEntity<>(s3ImgDeleteResult, HttpStatus.BAD_REQUEST);
                }
            }
            // todo: check fileService images
            bookService.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * This method handles the POST call to /books which create a new book.
     * It checks for authorization and user's request body format.
     */
    @PostMapping(produces = "application/json", consumes = "application/json")
    public @ResponseBody ResponseEntity<?> createBook(HttpServletRequest request,
                                                        @RequestBody String jsonBook) throws JsonProcessingException {
        // check for authorization
        String header = request.getHeader("Authorization");
        HashMap<String, String> authResult = userAuthorization.check(header);
        if (!authResult.get("status").equals("200")) {
            return noAuthResponse(authResult);
        }

        // new user information as hashmap
        HashMap<String, String> mapBook = new ObjectMapper().readValue(jsonBook, new TypeReference<>(){});
        // check request body validity
        HashMap<String, String> reqBodyCheckResult = bookRequestBodyValidator.checkForPost(mapBook);
        if (reqBodyCheckResult.containsKey("error")) { // return http 400 if request body is invalid
            return new ResponseEntity<>(reqBodyCheckResult, HttpStatus.BAD_REQUEST);
        }

        // create new book
        Book tmpBook = new Book(mapBook.get("title"), mapBook.get("author"),
                mapBook.get("isbn"), mapBook.get("published_date"));
        tmpBook.setUser_id(authResult.get("id"));
        String currTimestamp = formatter.format(new Date());
        tmpBook.setBook_created(currTimestamp);

        // save the book and return response http 201
        bookService.save(tmpBook);
        HashMap<String, Object> response = tmpBook.serializeToMap();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    /**
     * This method process the API response if authorization is invalid.
     */
    private ResponseEntity<?> noAuthResponse(HashMap<String, String> authRes) {
        if (authRes.get("status").equals("401")) {
        // return http 401 if username or password is invalid
            authRes.remove("status");
            return new ResponseEntity<>(authRes,HttpStatus.UNAUTHORIZED);
        } else {
        // return http 400 if authentication format is invalid
            authRes.remove("status");
            return new ResponseEntity<>(authRes,HttpStatus.BAD_REQUEST);
        }
    }

}
