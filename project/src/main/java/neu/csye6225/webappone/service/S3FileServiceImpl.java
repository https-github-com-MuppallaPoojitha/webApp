package neu.csye6225.webappone.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@Service("s3FileService")
public class S3FileServiceImpl implements  S3FileService {

    @Autowired
    private AmazonS3 s3client;
    @Value("${s3.bucketName}")
    private String bucketName;

    /**
     * Delete image inside S3 bucket.
     * @return
     */
    @Override
    public HashMap<String, String> deleteFile(String s3_object_name) {
        HashMap<String, String> deleteRes = new HashMap<>();
        try {
            s3client.deleteObject(bucketName,s3_object_name);
            deleteRes.put("ok","");
        } catch (AmazonServiceException ase) {
            deleteRes.put("Error", "Amazon Service Exception");
            deleteRes.put("Error Content", ase.getMessage());
            deleteRes.put("AWS Error Code", ase.getErrorCode());
            deleteRes.put("AWS Error Type", ase.getErrorType().toString());
        } catch (AmazonClientException ace) {
            deleteRes.put("Error", "Amazon Client Exception");
            deleteRes.put("Error Content", ace.getMessage());
        }
        return deleteRes;
    }

    /**
     * Upload image to s3 bucket.
     */
    @Override
    public HashMap<String, String> uploadFile(String s3_object_name, MultipartFile uploadedFile) {
        HashMap<String, String> uploadRes = new HashMap<>();
        File file = null;
        try {
            file = multipartToFile(uploadedFile);
            s3client.putObject(bucketName, s3_object_name, file);
            uploadRes.put("ok","");
        } catch (AmazonServiceException ase) {
            uploadRes.put("Error", "Amazon Service Exception");
            uploadRes.put("Error Content", ase.getMessage());
            uploadRes.put("AWS Error Code", ase.getErrorCode());
            uploadRes.put("AWS Error Type", ase.getErrorType().toString());
        } catch (AmazonClientException ace) {
            uploadRes.put("Error", "Amazon Client Exception");
            uploadRes.put("Error Content", ace.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadRes;
    }

    private File multipartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }
}
