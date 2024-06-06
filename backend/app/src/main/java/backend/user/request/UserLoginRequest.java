package backend.user.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String username;
    private String password;

    // getter和setter方法
}
