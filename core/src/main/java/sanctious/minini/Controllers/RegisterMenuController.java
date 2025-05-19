package sanctious.minini.Controllers;

public class RegisterMenuController {

    public static boolean isPasswordStrong(String password){
        return password != null &&
            password.length() >= 8 &&
            password.matches(".*[A-Z].*") &&
            password.matches(".*\\d.*") &&
            password.matches(".*[@%$#&*()_].*");
    }

    public void register(String username,
                         String email,
                         String password,
                         String passwordConfirm){

    }
}
