package neu.csye6225.webappone.utils.validation;

import neu.csye6225.webappone.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

@Component
public class BookRequestBodyValidator {

    @Autowired
    private BookService bookService;

    public HashMap<String, String> checkForPost(HashMap<String, String> bookInput) {
        HashMap<String, String> response = new HashMap<String, String>();
        HashSet<String> month = new HashSet<String>(Arrays
                .asList("jan", "feb", "mar", "apr", "may", "jun", "jul",
                        "aug", "sep", "oct", "nov", "dec", "january", "february",
                        "march", "april", "june", "july", "august", "september",
                        "october", "november", "december"));

        if (!bookInput.containsKey("title") || bookInput.get("title").isBlank()) {
        // check if title is not null, not empty and not blank
            response.put("error", "Please do not leave the book 'title' empty!");
        } else if (!bookInput.containsKey("author") || bookInput.get("author").isBlank()) {
        // check if author is not null, not empty and not blank
            response.put("error", "Please do not leave the book 'author' empty!");
        } else if (!bookInput.containsKey("isbn") || !bookInput.get("isbn").matches("\\d{3}+-\\d{10}+")) {
        // check if isbn is a not null and follows a correct ISBN 13 format
            response.put("error", "Please enter a valid 'isbn' in the format of XXX-XXXXXXXXXX.");
        } else if (bookService.findByIsbn(bookInput.get("isbn")) != null) {
        // check if isbn is unique
            response.put("error", "There is already a book with isbn " + bookInput.get("isbn"));
        } else if (!bookInput.containsKey("published_date") || bookInput.get("published_date").isBlank()) {
        // check if the published date is not null
            response.put("error", "Please do not leave the 'published_date' empty!");
        } else {
            String[] monthYear = bookInput.get("published_date").split(", ");
            if (!month.contains(monthYear[0].toLowerCase()) || !monthYear[1].matches("\\d{4}")) {
            // check if the published date is in valid format
                response.put("error",
                        "Please enter 'published_date' in the format of 'MMM, YYYY', month in characters only.");
            }
        }

        if (!response.containsKey("error")){
        // if request body passes all requirements
            response.put("ok", "");
        }
        return response;
    }
}
