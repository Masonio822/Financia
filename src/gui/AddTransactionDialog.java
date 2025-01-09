package gui;

import data.Transaction;
import data.user.LoginUser;
import gui.observer.Notifier;
import main.ResourceLoader;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddTransactionDialog extends JDialog {
    public AddTransactionDialog() {
        super();
        setIconImage(new ImageIcon("src\\resources\\assets\\logo.png").getImage());
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setTitle("Add Transaction");
        setSize(400, 350);
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 10, 5);

        gbc.gridy = 1;
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        add(getHeader(), gbc);
        gbc.weightx = 0.0;
        gbc.gridwidth = 1;

        gbc.gridy = 2;
        gbc.gridx = 2;
        add(new JLabel("Company: "), gbc);
        gbc.gridx = 3;
        JTextField companyField = createField();
        add(companyField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 2;
        add(new JLabel("Amount:  "), gbc);
        gbc.gridx = 3;
        JTextField amountField = createField();
        amountField.setText("$");
        amountField.setCaretPosition(1);
        ((AbstractDocument) amountField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("^[0-9.$-]+$")) { //Conditions
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && text.matches("^[0-9.$-]+$")) { //Conditions
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        amountField.setPreferredSize(new Dimension(55, 20));
        add(amountField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        add(new JLabel("Date: "), gbc);
        gbc.gridx = 3;
        JTextField dateField = createField();
        //Get the date
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        dateField.setText(todayDate);
        dateField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (dateField.getText().equals(todayDate)) {
                    dateField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (dateField.getText().isEmpty()) {
                    dateField.setText(todayDate);
                }
            }
        });
        add(dateField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        add(new JLabel("Reoccurring: "), gbc);
        gbc.gridx = 3;
        JCheckBox reoccurringCheckBox = new JCheckBox();
        add(reoccurringCheckBox, gbc);

        JLabel error = new JLabel();
        error.setForeground(Color.RED);

        gbc.gridy = 6;
        gbc.gridx = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JButton createButton = new JButton("Create");
        createButton.setPreferredSize(new Dimension(133, 35));
        createButton.addActionListener(l -> {
            try {
                Transaction transaction = new Transaction(
                        companyField.getText(),
                        Double.parseDouble(amountField.getText().replaceAll("\\$", "")),
                        LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                        reoccurringCheckBox.isSelected()
                );
                LoginUser.getLoggedInUser().addTransaction(transaction);
                Notifier.update();
                LoginUser.getLoggedInUser().save();
                this.dispose();
            } catch (DateTimeParseException dateFormatException) {
                error.setText("Invalid Date!");
                error.setVisible(true);
            } catch (NumberFormatException numberFormatException) {
                error.setText("Invalid Amount!");
                error.setVisible(true);
            } catch (NullPointerException npe) {
                dispose();
            }
        });
        add(createButton, gbc);

        gbc.gridy = 6;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(133, 35));
        cancelButton.addActionListener(l -> setVisible(false));
        add(cancelButton, gbc);

        gbc.gridy = 7;
        gbc.gridwidth = 3;
        add(error, gbc);
    }

    private JLabel getHeader() {
        JLabel heading = new JLabel("Add Transaction");
        heading.setFont(ResourceLoader.getInstance().getRighteousFont(30));
        return heading;
    }

    private JTextField createField() {
        JTextField field = new JTextField();
        field.setFocusTraversalKeysEnabled(true);
        field.setPreferredSize(new Dimension(100, 20));
        field.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        return field;
    }
}
