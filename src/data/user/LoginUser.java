package data.user;

import gui.screen.LoginScreen;
import gui.screen.MainScreen;
import main.App;

import java.util.Arrays;

public class LoginUser {
    private static User loggedInUser;

    private LoginUser() {}

    public static boolean login(User user, char[] password) {
        if (user == null) {
            return false;
        }
        if (!(
                //Needed qualifications to login
                Arrays.toString(password).equals(user.getPassword())
                && UserHelper.contains(user)
        )) return false;
        loggedInUser = user;
        App.getInstance().switchScreen(new MainScreen());
        return true;
    }

    public static void logout() {
        loggedInUser = null;
        App.getInstance().switchScreen(new LoginScreen());
    }

    public static boolean isLoggedIn() {
        return loggedInUser == null;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }
}
