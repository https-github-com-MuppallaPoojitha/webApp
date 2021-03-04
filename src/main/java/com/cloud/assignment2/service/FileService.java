package com.cloud.assignment2.service;
import com.cloud.assignment2.model.File;



import java.util.List;

public interface FileService {
    File save(File file);
    File findByFileId(String fileId);
    List<File> findByBookId(String bookId);
    void deleteByFileId(String fileId);
}
