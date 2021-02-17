package neu.csye6225.WebApp.Repository;

import neu.csye6225.WebApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User save(User user);
    User findByUsernameIgnoreCase(String username);
}
