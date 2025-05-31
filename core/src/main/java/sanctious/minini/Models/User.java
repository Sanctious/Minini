package sanctious.minini.Models;

public class User {
    private String username;
    private String securityQuestion;
    private String password;
    private String avatar;
    private UserScores data = new UserScores();

    public User(String username, String password, String securityQuestion) {
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.avatar = GameAPI.getAssetManager().getRandomAvatar();
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public UserScores getData() {
        return data;
    }
}
