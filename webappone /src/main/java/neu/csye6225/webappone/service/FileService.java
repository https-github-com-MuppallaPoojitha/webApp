package neu.csye6225.webappone.service;

import neu.csye6225.webappone.pojo.File;

import java.util.List;

public interface FileService {
    File save(File file);
    File findByFileId(String fileId);
    List<File> findByBookId(String bookId);
    void deleteByFileId(String fileId);
}