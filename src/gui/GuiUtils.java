package gui;

import javax.swing.*;
import java.awt.*;

public class GuiUtils {
    private GuiUtils() {}

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    public static JPanel group(int align, Component... components) {
        JPanel panel = new JPanel();
        switch (align) {
            case VERTICAL -> panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            case HORIZONTAL -> panel.setLayout(new FlowLayout());
            default -> throw new RuntimeException("Invalid alignment type!");
        }
        for (Component c : components) {
            panel.add(c);
        }
        return panel;
    }

    public static JPanel align(int align, Container... containers) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        switch (align) {
            case LEFT -> panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            case RIGHT -> panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            default -> throw new RuntimeException("Invalid alignment type!");
        }
        for (Container c : containers) {
            panel.add(c);
        }
        return panel;
    }

    public static GridBagConstraints cleanGbc(GridBagConstraints gbc) {
        return new GridBagConstraints(
                gbc.gridx,
                gbc.gridy,
                0,
                0,
                0,
                0,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10),
                0,
                0
        );
    }

    public static Image resizeImage(Image image, Dimension size) {
        return image.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
    }
    public static ImageIcon resizeImage(ImageIcon icon, Dimension size) {
        return new ImageIcon(icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH));
    }
}
