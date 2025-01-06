package data.user;

import gui.screen.LoginScreen;
import gui.screen.MainScreen;
import main.App;

import java.util.Arrays;

public class LoginUser extends User {
    private User loggedInUser;
    private static final LoginUser instance = new LoginUser();

    private LoginUser() {}

    public boolean login(User user, char[] password) {
        if (!(
                //Needed qualifications to login
                Arrays.toString(password).equals(user.getPassword())
                && UserHelper.contains(user)
        )) return false;
        loggedInUser = user;
        App.getInstance().switchScreen(new MainScreen());
        return true;
    }

    public void logout() {
        loggedInUser = null;
        App.getInstance().switchScreen(new LoginScreen());
    }

    public boolean isLoggedIn() {
        return loggedInUser == null;
    }

    public static LoginUser getInstance() {
        return instance;
    }
}
