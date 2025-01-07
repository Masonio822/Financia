package gui.screen;

import data.user.LoginUser;
import data.user.User;
import data.user.UserHelper;
import gui.RoundedBorder;
import main.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class LoginScreen implements Screen {
    JPanel loginScreen = new JPanel(new GridBagLayout());

    public LoginScreen() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        loginPanel.add(new JLabel("Username: "), gbc);
        gbc.gridx = 1;
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(100, 20));
        usernameField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        usernameField.setFocusTraversalKeysEnabled(true);
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password: "), gbc);
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(100, 20));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        passwordField.setFocusTraversalKeysEnabled(true);
        loginPanel.add(passwordField, gbc);

        JLabel error = new JLabel();
        error.setForeground(Color.RED);
        error.setVisible(false);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(createButton("Register", l -> {
            if (usernameField.getText().isBlank() || Arrays.toString(passwordField.getPassword()).isBlank()) {
                error.setText("The username or password contains invalid characters!");
                error.setVisible(true);
            } else {
                User user = new User(usernameField.getText(), Arrays.toString(passwordField.getPassword()));
                user.save();
                UserHelper.cacheUsers();
            }
        }), gbc);

        gbc.gridx = 1;
        loginPanel.add(createButton("Login", l -> {
            if (!(UserHelper.containsIgnoreUuid(new User(usernameField.getText(), Arrays.toString(passwordField.getPassword())))
                    && LoginUser.login(UserHelper.match(usernameField.getText(), Arrays.toString(passwordField.getPassword())), passwordField.getPassword()))) {
                error.setText("Could not find that user!");
                error.setVisible(true);
            }
        }), gbc);

        GridBagConstraints highGbc = new GridBagConstraints();
        highGbc.ipadx = 20;
        highGbc.ipady = 20;

        JLabel title = new JLabel("Financia");
        title.setVerticalTextPosition(JLabel.TOP);
        title.setFont(ResourceLoader.getInstance().getRighteousFont(200));
        title.setForeground(new Color(224, 159, 54));
        highGbc.fill = GridBagConstraints.HORIZONTAL;
        highGbc.anchor = GridBagConstraints.NORTH;
        loginScreen.add(title, highGbc);

        highGbc.gridy = 1;
        highGbc.anchor = GridBagConstraints.CENTER;
        loginScreen.add(loginPanel, highGbc);

        highGbc.gridy = 2;
        highGbc.fill = GridBagConstraints.VERTICAL;
        loginScreen.add(error, highGbc);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setBorder(new RoundedBorder(10));
        button.setPreferredSize(new Dimension(100, 25));
        button.addActionListener(listener);

        return button;
    }


    @Override
    public Container getContentPane() {
        return loginScreen;
    }
}
