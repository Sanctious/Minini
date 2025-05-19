package sanctious.minini.Models;

public class GameAPI {

    private static final UserRegistry userRegistry = new UserRegistry();

    public static UserRegistry getUserRegistry() {
        return userRegistry;
    }
}
