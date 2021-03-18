package neu.csye6225.webappone.dao;

import neu.csye6225.webappone.pojo.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FileDao extends JpaRepository<File, Long> {
    File save(File file);
    File findByFileId(String fileId);
    List<File> findByBookId(String bookId);
    void deleteByFileId(String fileId);
}
