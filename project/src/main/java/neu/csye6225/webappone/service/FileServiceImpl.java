package neu.csye6225.webappone.service;

import neu.csye6225.webappone.dao.FileDao;
import neu.csye6225.webappone.pojo.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.timgroup.statsd.StatsDClient;

import java.util.List;

@Service("fileService")
public class FileServiceImpl implements FileService {
    @Autowired
    private FileDao fileDao;
    @Autowired
    private StatsDClient statsd;

    @Override
    public File save(File file) {
        long startTime = System.currentTimeMillis();
        File res = fileDao.save(file);
        statsd.recordExecutionTime("DB Response Time - Save File", System.currentTimeMillis() - startTime);
        return res;
    }

    @Override
    public File findByFileId(String fileId) {
        long startTime = System.currentTimeMillis();
        File res = fileDao.findByFileId(fileId);
        statsd.recordExecutionTime("DB Response Time - Find File By Id", System.currentTimeMillis() - startTime);
        return res;
    }

    @Override
    public List<File> findByBookId(String bookId) {
        long startTime = System.currentTimeMillis();
        List<File> res = fileDao.findByBookId(bookId);
        statsd.recordExecutionTime("DB Response Time - Find File By Book", System.currentTimeMillis() - startTime);
        return res;
    }

    @Override
    public void deleteByFileId(String fileId) {
        long startTime = System.currentTimeMillis();
        fileDao.deleteByFileId(fileId);
        statsd.recordExecutionTime("DB Response Time - Delete File", System.currentTimeMillis() - startTime);
    }

}

