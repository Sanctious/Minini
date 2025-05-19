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

    public static ViewResult register(String username,
                                      String password){
        User user = GameAPI.getUserRegistry().findUserByUsername(username);

        if (user != null) {
            return ViewResult.failure("This user already exists!");
        }
        if (!isPasswordStrong((password))){
            return ViewResult.failure("Your password isn't strong enough!");
        }

        return ViewResult.success("");
    }
}
