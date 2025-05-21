package sanctious.minini.Models;

public class GameAPI {
    private static final UserRegistry userRegistry = new UserRegistry();
    private static final AssManager assetManager = new AssManager();

    public static UserRegistry getUserRegistry() {
        return userRegistry;
    }

    public static AssManager getAssetManager(){
        return assetManager;
    }
}
