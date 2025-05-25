package sanctious.minini.Models;

public class User {
    private String username;
    private String password;
    private String avatar;
    private UserScores data = new UserScores();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public UserScores getData() {
        return data;
    }
}
