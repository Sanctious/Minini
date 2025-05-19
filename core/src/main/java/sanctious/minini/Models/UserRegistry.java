package sanctious.minini.Models;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class UserRegistry {
    private final List<User> users = new ArrayList<>();

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
}
