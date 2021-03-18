package neu.csye6225.webappone.service;

import neu.csye6225.webappone.pojo.User;

public interface UserService {
    User save(User user);
    User findByUsernameIgnoreCase(String username);
}
