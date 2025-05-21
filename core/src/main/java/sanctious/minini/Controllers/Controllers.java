package sanctious.minini.Controllers;

public class Controllers {
    private final static RegisterMenuController registerMenuController = new RegisterMenuController();
    private final static LoginMenuController loginMenuController = new LoginMenuController();


    public static RegisterMenuController getRegisterController(){
        return registerMenuController;
    }
    public static LoginMenuController getLoginMenuController(){
        return loginMenuController;
    }

}
