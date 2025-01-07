package data.user;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UserHelper {
    private static Set<User> cachedUsers;
    public static boolean addUserToCache(User user) {
        if (cachedUsers.contains(user)) {
            return false;
        }
        cachedUsers.add(user);
        return true;
    }
    public static void cacheUsers() {
        cachedUsers = new HashSet<>();

        File usersDir = new File(System.getenv("ProgramFiles") + "\\Financia\\users");
        File[] userList = usersDir.listFiles();

        if (userList != null) {
            for (File user : userList) {
                try (
                        FileInputStream fileIS = new FileInputStream(user);
                        ObjectInputStream objectIS = new ObjectInputStream(fileIS)
                ) {
                    cachedUsers.add((User) objectIS.readObject());
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new RuntimeException("Could not find user list!");
        }
    }

    public static boolean contains(User user) {
        if (cachedUsers == null) {
            cacheUsers();
        }
        for (User compare : cachedUsers) {
            if (user.equals(compare)) return true;
        }
        return false;
    }

    public static boolean containsIgnoreUuid(User user) {
        if (cachedUsers == null) {
            cacheUsers();
        }
        for (User compare : cachedUsers) {
            if (user.equalsIgnoreUuid(compare)) return true;
        }
        return false;
    }

    public static User match(String username, String password) {
        ArrayList<User> userList = new ArrayList<>(1);
        for (User u : cachedUsers) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                userList.add(u);
            }
        }
        if (userList.size() > 1) {
            throw new RuntimeException("Multiple matching users found!");
        }
        return userList.isEmpty() ? null : userList.getFirst();
    }
}
