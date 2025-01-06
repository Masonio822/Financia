package data.user;

public class UserNotLoggedIn extends RuntimeException {
    public UserNotLoggedIn(String message) {
        super(message);
    }

    public UserNotLoggedIn() {
        super("Could not find logged in user!");
    }
}
