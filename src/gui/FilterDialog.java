package gui;

import gui.observer.Notifier;
import main.App;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilterDialog extends JDialog {
    private final JComboBox<String> selections;
    private final ArrayList<Component> tempComponents = new ArrayList<>();
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JButton applyButton = new JButton("Apply");

    public FilterDialog(String[] fields) {
        super(App.getInstance().getFrame());
        this.setUndecorated(true);
        this.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setLayout(new GridBagLayout());

        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;

        selections = new JComboBox<>(fields);
        selections.addActionListener(l -> update());
        this.add(GuiUtils.group(GuiUtils.HORIZONTAL, new JLabel("Field: "), selections), gbc);

        gbc.gridwidth = 1;
        update();

        gbc.gridy = 4;
        gbc.gridx = 1;
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(l -> {
            this.dispose();
            Notifier.filter(null);
        });
        this.add(clearButton, gbc);

        gbc.gridx = 2;
        this.add(applyButton, gbc);

        this.pack();
    }

    private void update() {
        for (Component component : tempComponents) {
            this.remove(component);
        }
        for (ActionListener l : applyButton.getActionListeners()) {
            applyButton.removeActionListener(l);
        }

        if (selections.getSelectedItem() == null) {
            selections.setSelectedIndex(1);
        }
        switch (selections.getSelectedItem().toString()) {
            case "Reoccurring" -> {
                JLabel label = new JLabel("Reoccurring: ");
                JCheckBox box = new JCheckBox();
                tempComponents.add(label);
                tempComponents.add(box);
                gbc.gridy = 2;
                gbc.gridx = 1;
                this.add(label, gbc);
                gbc.gridx = 2;
                this.add(box, gbc);

                applyButton.addActionListener(l -> Notifier.filter(new RowFilter<>() {
                    @Override
                    public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                        return (Boolean) entry.getValue(2);
                    }
                }));
            }
            case "Date" -> {
                JLabel before = new JLabel("Before: ");
                tempComponents.add(before);
                gbc.gridy = 2;
                gbc.gridx = 1;
                this.add(before, gbc);

                JLabel after = new JLabel("After: ");
                tempComponents.add(after);
                gbc.gridy = 3;
                this.add(after, gbc);

                JTextField beforeField = new JTextField();
                beforeField.setFocusTraversalKeysEnabled(true);
                beforeField.setPreferredSize(new Dimension(75, 20));
                beforeField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                tempComponents.add(beforeField);
                beforeField.setText("yyyy/MM/dd");
                beforeField.setForeground(Color.GRAY);
                beforeField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (beforeField.getText().equals("yyyy/MM/dd")) {
                            beforeField.setText("");
                            beforeField.setForeground(Color.BLACK);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (beforeField.getText().isEmpty()) {
                            beforeField.setText("yyyy/MM/dd");
                            beforeField.setForeground(Color.GRAY);
                        }
                    }
                });
                gbc.gridx = 2;
                gbc.gridy = 2;
                this.add(beforeField, gbc);

                JTextField afterField = new JTextField();
                afterField.setFocusTraversalKeysEnabled(true);
                afterField.setPreferredSize(new Dimension(75, 20));
                afterField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                tempComponents.add(afterField);
                afterField.setText("yyyy/MM/dd");
                afterField.setForeground(Color.GRAY);
                afterField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (afterField.getText().equals("yyyy/MM/dd")) {
                            afterField.setText("");
                            afterField.setForeground(Color.BLACK);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (afterField.getText().isEmpty()) {
                            afterField.setText("yyyy/MM/dd");
                            afterField.setForeground(Color.GRAY);
                        }
                    }
                });
                gbc.gridy = 3;
                gbc.gridx = 2;
                this.add(afterField, gbc);

                applyButton.addActionListener(l -> {
                    List<RowFilter<TableModel, Integer>> filters = new ArrayList<>();
                    try {
                        //Max filter
                        if (!(beforeField.getText().isEmpty() || beforeField.getText().equals("yyyy/MM/dd"))) {
                            Date date = Date.from(LocalDate
                                    .parse(beforeField.getText(), DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                                    .atStartOfDay(ZoneId.systemDefault())
                                    .toInstant());
                            filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, date, 2));
                        }
                        //Min filter
                        if (!(afterField.getText().isEmpty() || afterField.getText().equals("yyyy/MM/dd"))) {
                            Date date = Date.from(LocalDate
                                    .parse(afterField.getText(), DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                                    .atStartOfDay(ZoneId.systemDefault())
                                    .toInstant());
                            filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, date, 2));
                        }
                    } catch (DateTimeParseException e) {
                        JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy/MM/dd.");
//                        this.dispose();
                    }
                    Notifier.filter(RowFilter.andFilter(filters));
                    this.dispose();
                });
            }
            case "Amount" -> {
                JLabel min = new JLabel("Min <");
                tempComponents.add(min);
                gbc.gridx = 1;
                gbc.gridy = 2;
                this.add(min, gbc);

                JLabel max = new JLabel("Max >");
                tempComponents.add(max);
                gbc.gridy = 3;
                this.add(max, gbc);

                JTextField minField = createAmountField();
                tempComponents.add(minField);
                minField.setToolTipText("Excludes all values less than the provided value");
                gbc.gridx = 2;
                gbc.gridy = 2;
                this.add(minField, gbc);

                JTextField maxField = createAmountField();
                tempComponents.add(maxField);
                maxField.setToolTipText("Excludes all values greater than the provided value");
                gbc.gridx = 2;
                gbc.gridy = 3;
                this.add(maxField, gbc);

                applyButton.addActionListener(l -> {
                    List<RowFilter<TableModel, Integer>> filters = new ArrayList<>();
                    try {
                        //Max filter
                        if (!(minField.getText().isEmpty())) {
                            filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, Double.parseDouble(minField.getText())));
                        }
                        //Min filter
                        if (!(maxField.getText().isEmpty())) {
                            filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, Double.parseDouble(maxField.getText())));
                        }
                    } catch (NumberFormatException e) {
                        this.dispose();
                    }
                    Notifier.filter(RowFilter.andFilter(filters));
                    this.dispose();
                });
            }
            case "Company" -> {
                JLabel label = new JLabel("Company: ");
                tempComponents.add(label);
                gbc.gridy = 2;
                gbc.gridx = 1;
                this.add(label, gbc);

                JTextField field = new JTextField();
                tempComponents.add(field);
                field.setFocusTraversalKeysEnabled(true);
                field.setPreferredSize(new Dimension(100, 20));
                field.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                gbc.gridx = 2;
                this.add(field, gbc);

                applyButton.addActionListener(l -> Notifier.filter(RowFilter.regexFilter("(?i)" + field.getText())));
            }
            default -> throw new RuntimeException("Unhandled combo box value!");
        }

        this.pack();
    }

    private JTextField createAmountField() {
        JTextField amountField = new JTextField();
        amountField.setFocusTraversalKeysEnabled(true);
        amountField.setPreferredSize(new Dimension(75, 25));
        amountField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
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

        return amountField;
    }
}