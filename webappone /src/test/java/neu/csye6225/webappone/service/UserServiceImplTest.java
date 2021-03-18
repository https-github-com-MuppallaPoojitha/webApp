package neu.csye6225.webappone.service;

import neu.csye6225.webappone.dao.UserDao;
import neu.csye6225.webappone.pojo.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {
    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserDao userDao;

    private User user = new User("Jane", "Doe", "janeDoe@example.com", "12345aA!");

    @Before
    public void setUp() {
        Mockito.when(userDao.findByUsernameIgnoreCase(user.getUsername())).thenReturn(user);
        Mockito.when(userDao.save(user)).thenReturn(user);
    }

    @Test
    public void whenValidUsername_thenUserShouldBeFound() {
        String username = "janeDoe@example.com";
        User found = userService.findByUsernameIgnoreCase(username);
        assertThat(found.getUsername()).isEqualTo(username);
    }

    @Test
    public void whenSavedUsername_thenUserShouldBeSaved() {
        User newUser = userDao.save(user);
        assertEquals(user.getUsername(), newUser.getUsername());
    }
}
