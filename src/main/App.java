package main;

import gui.screen.LoginScreen;
import gui.screen.Screen;

import javax.swing.*;

public class App {
    private final JFrame frame = new JFrame("Financia");
    private static final App instance = new App();
    private Screen currentScreen;

    public App() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(ResourceLoader.getInstance().getLogo().getImage());

        switchScreen(new LoginScreen());
    }

    public void show() {
        frame.setVisible(true);
    }

    public void switchScreen(Screen screen) {
        frame.setContentPane(screen);
        frame.revalidate();
        frame.repaint();
    }

    public JFrame getFrame() {
        return frame;
    }

    public static App getInstance() {
        return instance;
    }
}
