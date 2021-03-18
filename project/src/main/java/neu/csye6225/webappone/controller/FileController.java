package neu.csye6225.webappone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

@Controller
@RestController
@RequestMapping("/books")
public class FileController {

    @Autowired
    private BookService bookService;
    @Autowired
    private FileService fileService;
    @Autowired
    private S3FileService s3FileService;
    @Autowired
    private UserAuthorization userAuthorization;
    @Autowired
    private FileValidator fileValidator;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");


    /**
     * This method handles the POST call that creates a new image to a book.
     * It checks for authorization and user's input file.
     */
    @PostMapping(value = "/{bookId}/image", produces = "application/json")
    public @ResponseBody
    ResponseEntity<?> uploadImage(
            @PathVariable("bookId") String bookId, @RequestParam(value = "file") MultipartFile file,
            HttpServletRequest request){
        HashMap<String, String> errMsg = new HashMap<>();

        // check for authorization
        String header = request.getHeader("Authorization");
        HashMap<String, String> authResult = userAuthorization.check(header);
        if (!authResult.get("status").equals("200")) {
            return noAuthResponse(authResult);
        }

        // check for book id validity
        Book book = bookService.findById(bookId);
        if (book == null) {
            errMsg.put("error", "There is no book found with id " + bookId);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        }

        // check for file input
        HashMap<String, String> fileCheckResult = fileValidator.checkForPost(book.getBook_images(), file);
        if (fileCheckResult.containsKey("error")) {
            return new ResponseEntity<>(fileCheckResult, HttpStatus.BAD_REQUEST);
        }
        File tmpFile = new File(fileCheckResult.get("fileName"), authResult.get("id"), bookId,
                formatter.format(new Date()), book);

        // upload image to s3
        HashMap<String, String> s3UploadResult = s3FileService.uploadFile(tmpFile.getS3_object_name(), file);
        if (!s3UploadResult.containsKey("ok")) {
            return new ResponseEntity<>(s3UploadResult, HttpStatus.BAD_REQUEST);
        }

        // save image metadata and book, return http request
        fileService.save(tmpFile);
        book.addImage(tmpFile);
        bookService.save(book);
        HashMap<String, String> response = tmpFile.serializeToMap();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * This method handles the DELETE call to a particular image.
     */
    @Transactional
    @DeleteMapping("/{bookId}/image/{imgId}")
    public ResponseEntity<?> deleteImageById(HttpServletRequest request, @PathVariable String bookId,
                                             @PathVariable String imgId) {
        // check for authorization
        String header = request.getHeader("Authorization");
        HashMap<String, String> authResult = userAuthorization.check(header);
        if (!authResult.get("status").equals("200")) { // if auth is invalid
            return noAuthResponse(authResult);
        }

        // check for book id validity
        Book book = bookService.findById(bookId);
        HashMap<String, String> errMsg = new HashMap<>();
        if (book == null) {
            errMsg.put("error", "There is no book found with id " + bookId);
            return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
        } else {
            // check image in the database
            File uploadedFile = fileService.findByFileId(imgId);
            if (uploadedFile == null) {
                errMsg.put("error", "This image does not exist.");
                return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
            } else if (!uploadedFile.getUser_id().equals(authResult.get("id"))) { // attempt not from original user
                errMsg.put("error", "This image is not uploaded by you.");
                return new ResponseEntity<>(errMsg, HttpStatus.UNAUTHORIZED);
            }
            // delete image from s3
            HashMap<String, String> s3DeleteResult = s3FileService.deleteFile(bookId + "/" + imgId);
            if (!s3DeleteResult.containsKey("ok")) {
                return new ResponseEntity<>(s3DeleteResult, HttpStatus.BAD_REQUEST);
            }
            // delete image meta data from rds
            fileService.deleteByFileId(imgId);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
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
