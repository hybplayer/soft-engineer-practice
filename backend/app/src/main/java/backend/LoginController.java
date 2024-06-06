package backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private Map<String, String> users = new HashMap<>(){
        {
            put("admin", "123456");
        }
    };


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String password = users.get(request.getUsername());
        if (password != null && password.equals(request.getPassword())) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", request.getUsername());
            userInfo.put("avatar", "default-avatar.png"); // 默认头像
            userInfo.put("hobby", ""); // 默认爱好
            System.out.println("Login successful for user: " + request.getUsername()); // 添加日志
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("密码和确认密码不一致");
        }
        if (users.containsKey(request.getUsername())) {
            return ResponseEntity.badRequest().body("用户名已存在");
        }
        users.put(request.getUsername(), request.getPassword());
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", request.getUsername());
        userInfo.put("avatar", "default-avatar.png"); // 默认头像
        userInfo.put("hobby", ""); // 默认爱好
        System.out.println("Registration successful for user: " + request.getUsername()); // 添加日志
        return ResponseEntity.ok(userInfo);
    }

}
