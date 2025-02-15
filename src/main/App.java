package main;

import gui.Background;
import gui.GuiUtils;
import gui.screen.LoginScreen;
import gui.screen.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        frame.setContentPane(new Background(ResourceLoader.getInstance().getBackground()));
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                switchScreen(getCurrentScreen());
            }
        });


        switchScreen(new LoginScreen());
    }

    public void show() {
        frame.setVisible(true);
    }

    public void switchScreen(Screen screen) {
        //Get frame size using multi-monitor safe methods
        Dimension frameSize = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds()
                .getSize();
        //Get insets of screen
        Insets insets = Toolkit.getDefaultToolkit()
                .getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration());
        //Update frameSize to only use displayable space
        frameSize = new Dimension(
                frameSize.width - (insets.left + insets.right),
                frameSize.height - (insets.top + insets.bottom)
        );

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(frameSize);
        screen.setOpaque(false);
        screen.setBounds(0, 0, frameSize.width, frameSize.height);

        ImageIcon backgroundImg = GuiUtils.resizeImage(ResourceLoader.getInstance().getBackground(), frameSize);
        Background bg = new Background(backgroundImg);
        bg.setBounds(0, 0, frameSize.width, frameSize.height);

        layeredPane.add(bg, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(screen, JLayeredPane.PALETTE_LAYER);

        frame.setContentPane(layeredPane);
        this.currentScreen = screen;
        frame.revalidate();
        frame.repaint();
    }

    public JFrame getFrame() {
        return frame;
    }

    public static App getInstance() {
        return instance;
    }

    public Screen getCurrentScreen() {
        return this.currentScreen;
    }
}
