package sanctious.minini.Controllers;

public class Controllers {
    private final static RegisterMenuController registerMenuController = new RegisterMenuController();
    private final static LoginMenuController loginMenuController = new LoginMenuController();
    private final static GameController gameController = new GameController();


    public static RegisterMenuController getRegisterController(){
        return registerMenuController;
    }
    public static LoginMenuController getLoginMenuController(){
        return loginMenuController;
    }
    public static GameController getGameController(){
        return gameController;
    }

}
