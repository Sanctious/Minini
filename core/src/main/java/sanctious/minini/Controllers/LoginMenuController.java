package sanctious.minini.Controllers;

import sanctious.minini.Models.GameAPI;
import sanctious.minini.Models.User;
import sanctious.minini.Models.UserRegistry;
import sanctious.minini.Models.ViewResult;

public class LoginMenuController {
    public ViewResult<Void> login(String username,
                                   String password){
        User user = GameAPI.getUserRegistry().findUserByUsername(username);

        if (user == null) {
            return ViewResult.failure("This user doesn't exist!");
        }
        if (!password.equals(user.getPassword())){
            return ViewResult.failure("Incorrect password!");
        }

        GameAPI.getUserRegistry().setActiveUser(user);

        return ViewResult.success(null);
    }
}
