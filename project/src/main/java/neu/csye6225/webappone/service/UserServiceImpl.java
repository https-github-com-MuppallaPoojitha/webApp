package neu.csye6225.webappone.service;

import neu.csye6225.webappone.dao.UserDao;
import neu.csye6225.webappone.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public User findByUsernameIgnoreCase(String username) {
        return userDao.findByUsernameIgnoreCase(username);
    }
}
