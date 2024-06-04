package backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import backend.user.*;
import backend.user.request.UserRequest;

@SpringBootTest
public class UserSystemTest {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Test
    public void test1() {
        // create 10 new users
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setPassword("password" + i);
            userRepository.save(user);
        }

        // check if the users are saved
        for (int i = 0; i < 10; i++) {
            User user = userRepository.findByUsername("user" + i);
            Assertions.assertNotNull(user);
            Assertions.assertEquals("user" + i, user.getUsername());
            Assertions.assertEquals("password" + i, user.getPassword());
        }

        // delete the users
        for (int i = 0; i < 10; i++) {
            User user = userRepository.findByUsername("user" + i);
            userRepository.delete(user);
        }
    }

    @Test
    public void test2() {
        // test controller
        UserRequest request = new UserRequest();
        request.setUsername("test_user");
        request.setPassword("password");

        // delete user if exists
        User if_user = userRepository.findByUsername("test_user");
        if (if_user != null) {
            userRepository.delete(if_user);
        }

        // register user
        User registeredUser = userController.registerUser(request).getBody();
        Assertions.assertNotNull(registeredUser);
        Assertions.assertEquals("test_user", registeredUser.getUsername());
        Assertions.assertEquals("password", registeredUser.getPassword());

        // login user
        Long userID = userController.loginUser(request).getBody();
        Assertions.assertNotNull(userID);

        // get user
        User user = userController.getUser(userID).getBody();
        Assertions.assertNotNull(user);
        Assertions.assertEquals("test_user", user.getUsername());

        // update user
        user.setPassword("newpassword");
        User updatedUser = userController.updateUser(user).getBody();
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("newpassword", updatedUser.getPassword());

        // add trip preference
        userController.addTripPreference(userID, "trip1");
        userController.addTripPreference(userID, "trip2");
        user = userController.getUser(userID).getBody();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(2, user.getTripPreferences().size());

        // remove trip preference
        userController.removeTripPreference(userID, "trip1");
        user = userController.getUser(userID).getBody();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1, user.getTripPreferences().size());

        // delete user
        userRepository.delete(user);
    }

}
