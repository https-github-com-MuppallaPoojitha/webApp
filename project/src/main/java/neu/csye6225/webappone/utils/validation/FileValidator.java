package neu.csye6225.webappone.utils.validation;

import neu.csye6225.webappone.pojo.File;
import neu.csye6225.webappone.service.BookService;
import neu.csye6225.webappone.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@Component
public class FileValidator {
    @Autowired
    private BookService bookService;

    public HashMap<String, String> checkForPost(List<File> book_images, MultipartFile file) {
        HashMap<String, String> response = new HashMap<String, String>();
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();

        // check repetitive filename
        Boolean fileNameExists = false;
        for (File f : book_images) {
            if (f.getFileName().equals(fileName)) fileNameExists = true;
        }

        // check file input
        if (file == null) {
            response.put("error", "Please upload an image file.");
        } else if (fileNameExists) {
            response.put("error", "This image file already exists.");
        } else if (!fileType.contains("image")) {
            response.put("error", "Input file must be an image.");
        }

        if (!response.containsKey("error")){
            response.put("fileName", fileName);
        }
        return response;
    }
}
