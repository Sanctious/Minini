package sanctious.minini.Models;

public class GameAPI {
    private static final UserRegistry userRegistry = new UserRegistry();
    private static final AssetManager assetManager = new AssetManager();


    public static UserRegistry getUserRegistry() {
        return userRegistry;
    }

    public static AssetManager getAssetManager(){
        return assetManager;
    }
}
