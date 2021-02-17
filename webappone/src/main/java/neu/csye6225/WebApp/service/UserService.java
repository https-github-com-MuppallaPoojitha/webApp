package neu.csye6225.WebApp.service;

import neu.csye6225.WebApp.model.User;

public interface UserService {
    User save(User user);
    User findByUsernameIgnoreCase(String username);
}
