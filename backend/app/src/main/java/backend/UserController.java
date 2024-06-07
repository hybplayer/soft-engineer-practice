package backend;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final Map<String, User> userStorage = new HashMap<>();
    private final Map<Long, Destination> destinationStorage = new HashMap<>();
    private final AtomicLong destinationIdCounter = new AtomicLong();

    // 初始化用户数据
    @PostConstruct
    public void init() {
        User initialUser = new User();
        initialUser.setId(1L);
        initialUser.setUsername("admin");
        initialUser.setPassword("123456");
        initialUser.setHobby("Reading");
        userStorage.put(initialUser.getUsername(), initialUser);
        User usr1 = new User();
        usr1.setId(2L);
        usr1.setUsername("usr1");
        usr1.setPassword("123456");
        usr1.setHobby("playing");
        userStorage.put(usr1.getUsername(), usr1);
        User usr2 = new User();
        usr2.setId(3L);
        usr2.setUsername("usr2");
        usr2.setPassword("123456");
        usr2.setHobby("looking");
        userStorage.put(usr2.getUsername(), usr2);
    }

    @GetMapping("/allusers")
    public ResponseEntity<List<User>> getAllUsers() {
        // 返回所有用户
        return ResponseEntity.ok(userStorage.values().stream().toList());
    }

    // 获取用户信息
    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        logger.info("Fetching user with username: {}", username);
        User user = userStorage.get(username);
        if (user != null) {
            logger.info("User found: {}", user);
        } else {
            logger.info("User not found: {}", username);
        }
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // 更新用户信息
    @PostMapping("/users/update")
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateRequest request) {
        User user = userStorage.get(request.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (request.getNewNickname() != null) {
            userStorage.remove(user.getUsername());
            user.setUsername(request.getNewNickname());
            userStorage.put(user.getUsername(), user);
        }
        user.setPassword(request.getNewPassword() != null ? request.getNewPassword() : user.getPassword());
        user.setHobby(request.getHobby() != null ? request.getHobby() : user.getHobby());
        return ResponseEntity.ok(user);
    }

    // 其他方法省略...
}
