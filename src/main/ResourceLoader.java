package main;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ResourceLoader {
    private ResourceLoader() {}
    private static final ResourceLoader instance = new ResourceLoader();

    private Font righteousFont;

    public void load() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            righteousFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\resources\\fonts\\righteous.ttf"));
            ge.registerFont(righteousFont);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public Font getRighteousFont(int size) {
        return new Font(righteousFont.getName(), righteousFont.getStyle(), size);
    }
    public Font getRighteousFont() {
        return righteousFont;
    }

    public static ResourceLoader getInstance() {
        return instance;
    }
}
