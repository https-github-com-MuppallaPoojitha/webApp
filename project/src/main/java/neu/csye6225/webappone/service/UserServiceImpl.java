package neu.csye6225.webappone.service;

import com.timgroup.statsd.StatsDClient;
import neu.csye6225.webappone.dao.UserDao;
import neu.csye6225.webappone.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private StatsDClient statsd;

    @Override
    public User save(User user) {
        long startTime = System.currentTimeMillis();
        User res = userDao.save(user);
        statsd.recordExecutionTime("DB Response Time - Save User", System.currentTimeMillis() - startTime);
        return res;
    }

    @Override
    public User findByUsernameIgnoreCase(String username) {
        long startTime = System.currentTimeMillis();
        User res = userDao.findByUsernameIgnoreCase(username);
        statsd.recordExecutionTime("DB Response Time - Find User By Username", System.currentTimeMillis() - startTime);
        return res;
    }
}
