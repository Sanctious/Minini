package sanctious.minini.Controllers;

import sanctious.minini.Models.GameAPI;
import sanctious.minini.Models.User;
import sanctious.minini.Models.ViewResult;

public class RegisterMenuController {

    public static boolean isPasswordStrong(String password){
        return password != null &&
            password.length() >= 8 &&
            password.matches(".*[A-Z].*") &&
            password.matches(".*\\d.*") &&
            password.matches(".*[@%$#&*()_].*");
    }

    public ViewResult<Void> register(String username,
                                     String password,
                                     String securityQuestion){
        if (GameAPI.getUserRegistry().userExists(username)) {
            return ViewResult.failure("This user already exists!");
        }
        if (!isPasswordStrong((password))){
            return ViewResult.failure("Your password isn't strong enough!");
        }

        User user = new User(username, password, securityQuestion);

        GameAPI.getUserRegistry().addUser(user);

        return ViewResult.success(null);
    }
}
