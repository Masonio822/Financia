package main;

import data.user.UserHelper;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File(System.getenv("ProgramFiles") + "\\Financia\\users");
        if (!(file.exists())) {
            file.mkdirs();
        }
        //Makes code run in a safe thread
        SwingUtilities.invokeLater(() -> {
            UserHelper.cacheUsers();
            ResourceLoader.getInstance().load();

            App.getInstance().show();
        });
    }
}