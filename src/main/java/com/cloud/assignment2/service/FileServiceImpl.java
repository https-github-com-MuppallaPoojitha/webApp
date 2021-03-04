package com.cloud.assignment2.service;

import com.cloud.assignment2.Repository.FileDao;
import com.cloud.assignment2.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("fileService")
public class FileServiceImpl implements FileService {
    @Autowired
    private FileDao fileDao;

    @Override
    public File save(File file) {return fileDao.save(file); }

    @Override
    public File findByFileId(String fileId) {
        return fileDao.findByFileId(fileId);
    }

    @Override
    public List<File> findByBookId(String bookId) {
        return fileDao.findByBookId(bookId);
    }

    @Override
    public void deleteByFileId(String fileId) { fileDao.deleteByFileId(fileId); }

}

