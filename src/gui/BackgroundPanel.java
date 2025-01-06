package gui;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JComponent {
    private final Image image;

    public BackgroundPanel(Image i) {
        this.image = i;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
