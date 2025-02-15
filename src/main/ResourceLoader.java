package main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ResourceLoader {
    private ResourceLoader() {}
    private static final ResourceLoader instance = new ResourceLoader();

    private Font righteousFont;
    private ImageIcon searchIcon;
    private ImageIcon logoutIcon;
    private ImageIcon logo;
    private ImageIcon background;

    public void load() {
        try (InputStream is = ResourceLoader.class.getResourceAsStream("/fonts/righteous.ttf")) {
            logoutIcon = new ImageIcon(Objects.requireNonNull(ResourceLoader.class.getResource("/assets/logout_icon.png")));
            logo = new ImageIcon(Objects.requireNonNull(ResourceLoader.class.getResource("/assets/logo.png")));
            searchIcon = new ImageIcon(Objects.requireNonNull(ResourceLoader.class.getResource("/assets/search_icon.png")));
            background = new ImageIcon(Objects.requireNonNull(ResourceLoader.class.getResource("/assets/background.png")));

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            righteousFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(is));
            ge.registerFont(righteousFont);

        } catch (IOException | FontFormatException | NullPointerException e) {
            System.out.println("Resource could not be loaded!");
            throw new RuntimeException(e);
        }
    }

    public Font getRighteousFont(int size) {
        return new Font(righteousFont.getName(), righteousFont.getStyle(), size);
    }
    public Font getRighteousFont() {
        return righteousFont;
    }

    public ImageIcon getSearchIcon() {
        return searchIcon;
    }

    public ImageIcon getLogo() {
        return logo;
    }

    public ImageIcon getLogoutIcon() {
        return logoutIcon;
    }

    public ImageIcon getBackground() {
        return background;
    }

    public static ResourceLoader getInstance() {
        return instance;
    }
}
