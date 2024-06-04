package backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.user.*;
import backend.user.request.UserRegisterRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class UserSystemTest {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

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
    @Transactional
    public void test2() {
        // test controller
        UserRegisterRequest request = new UserRegisterRequest();
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
        user.setId(userID);
        User updatedUser = userController.updateUser(userID, user).getBody();
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

    @Test
    public void test3() {
        // test http request
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("test_user");
        request.setPassword("password");
        ObjectMapper objectMapper = new ObjectMapper();
        String base_url = "/api/users/";

        // delete user if exists
        User if_user = userRepository.findByUsername("test_user");
        if (if_user != null) {
            userRepository.delete(if_user);
        }

        // register user
        try {
            mockMvc.perform(MockMvcRequestBuilders.post(base_url + "register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // login user -- success
        Long userID = null;
        try {
            mockMvc.perform(MockMvcRequestBuilders.get(base_url + "login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
            userID = Long.parseLong(mockMvc.perform(MockMvcRequestBuilders.get(base_url + "login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andReturn().getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // login user -- fail
        request.setPassword("wrongpassword");
        try {
            mockMvc.perform(MockMvcRequestBuilders.get(base_url + "login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setPassword("password");

        // get user -- success
        try {
            mockMvc.perform(MockMvcRequestBuilders.get(base_url + userID))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get user -- fail
        try {
            mockMvc.perform(MockMvcRequestBuilders.get(base_url + "0"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // update user
        String email = "123@qq.com";
        User update_User = new User();
        update_User.setUsername("test_user");
        update_User.setPassword("newpassword");
        update_User.setEmail(email);
        try {
            mockMvc.perform(MockMvcRequestBuilders.put(base_url + userID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(update_User))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // add trip preference
        try {
            mockMvc.perform(MockMvcRequestBuilders.patch(base_url + userID + "/addTripPreference")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("trip1")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            mockMvc.perform(MockMvcRequestBuilders.patch(base_url + userID + "/addTripPreference")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("trip2")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // remove trip preference
        try {
            mockMvc.perform(MockMvcRequestBuilders.patch(base_url + userID + "/removeTripPreference")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("trip1")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // delete user
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete(base_url + userID))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
