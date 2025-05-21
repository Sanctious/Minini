package sanctious.minini.Models;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class UserRegistry {
    private final List<User> users = new ArrayList<>();
    private User activeUser = null;

    public List<User> getUsers() {
        return users;
    }

    public User findUserByUsername(String username){
        return users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }

    public boolean userExists(String username){
        return findUserByUsername(username) != null;
    }

    public void addUser(User user){
        this.users.add(user);
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }
}
