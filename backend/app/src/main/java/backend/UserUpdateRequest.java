package backend;

public class UserUpdateRequest {
    private String username;
    private String newNickname;
    private String newPassword;
    private String hobby;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewNickname() {
        return newNickname;
    }

    public void setNewNickname(String newNickname) {
        this.newNickname = newNickname;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }


}
