package neu.csye6225.webappone.dao;

import neu.csye6225.webappone.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User save(User user);
    User findByUsernameIgnoreCase(String username);
    User findById(String id);
}
