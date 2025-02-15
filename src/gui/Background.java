package gui;

import javax.swing.*;
import java.awt.*;

public class Background extends JPanel {
    private final Image image;

    public Background(Image img) {
        this.image = img;
    }

    public Background(ImageIcon img) {
        this.image = img.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}