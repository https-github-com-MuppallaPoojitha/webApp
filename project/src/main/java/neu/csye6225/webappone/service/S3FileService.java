package neu.csye6225.webappone.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

public interface S3FileService {
    HashMap<String, String> uploadFile(String s3_object_name, MultipartFile uploadFile);
    HashMap<String, String> deleteFile(String s3_object_name);
}
