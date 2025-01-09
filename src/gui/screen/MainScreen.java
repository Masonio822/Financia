package gui.screen;

import data.Transaction;
import data.user.LoginUser;
import gui.AddTransactionDialog;
import gui.GuiUtils;
import gui.observer.Notifier;
import gui.observer.Observer;
import main.App;
import main.ResourceLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class MainScreen implements Screen, Observer {
    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final JLabel balanceDisplay;
    private final DefaultTableModel transactionTableModel = new DefaultTableModel(new String[]{
        "Company",
        "Amount",
        "Date",
        "Reoccurring"
    }, 0) {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0 -> {return String.class;}
                case 1 -> {return Double.class;}
                case 2 -> {return LocalDate.class;}
                case 3 -> {return Boolean.class;}
                default -> {return null;}
            }
        }
    };
    private final TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(transactionTableModel);

    public MainScreen() {
        Notifier.addObserver(this);

        JLabel header = new JLabel("Financia");
        header.setFont(ResourceLoader.getInstance().getRighteousFont(52));
        header.setForeground(new Color(224, 159, 54));
        JLabel welcome = new JLabel("Welcome, " + LoginUser.getLoggedInUser().getUsername() + "!");
        mainPanel.add(GuiUtils.group(GuiUtils.VERTICAL, header, welcome), BorderLayout.NORTH);

        balanceDisplay = new JLabel();
        DecimalFormat df = new DecimalFormat("#,###.00");
        balanceDisplay.setText("Balance: " + df.format(LoginUser.getLoggedInUser().getBalance()));

        JScrollPane transactionList = getTable();
        JTextField searchField = getSearchField();

        JPanel searchGroup = GuiUtils.group(GuiUtils.HORIZONTAL,
                balanceDisplay,
                new JLabel(GuiUtils.resizeImage(new ImageIcon("src\\resources\\assets\\search_icon.png"), new Dimension(20, 20))),
                searchField
        );
        JPanel transactionGrouping = GuiUtils.group(GuiUtils.VERTICAL, searchGroup, transactionList);
        transactionGrouping.setBorder(new EmptyBorder(150, 100, 75, 100));
        mainPanel.add(transactionGrouping, BorderLayout.CENTER);

        JButton addTransactionButton = getTransactionButton();
        JPanel p = GuiUtils.align(GuiUtils.RIGHT, addTransactionButton);
        p.setBorder(new EmptyBorder(0, 0, 25, 50));
        mainPanel.add(p, BorderLayout.SOUTH);
    }

    private JScrollPane getTable() {
        resetTableValues();
        JTable transactionTable = new JTable(transactionTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable.getTableHeader().setReorderingAllowed(false);
        transactionTable.setShowHorizontalLines(true);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setRowSorter(rowSorter);

        return new JScrollPane(transactionTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    private JButton getTransactionButton() {
        JButton addTransactionButton = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(224, 159, 54));
                g.fillOval(0, 0, this.getWidth(), this.getHeight());

                g.setColor(Color.BLACK);
                g.fillRect(23, 10, 5, 30);
                g.fillRect(10, 22, 30, 5);
            }
        };
        addTransactionButton.setPreferredSize(new Dimension(50, 50));
        addTransactionButton.setMinimumSize(new Dimension(50, 50));
        addTransactionButton.setMaximumSize(new Dimension(50, 50));

        addTransactionButton.setContentAreaFilled(false);
        addTransactionButton.setBorderPainted(false);
        addTransactionButton.setFocusPainted(false);
        addTransactionButton.setOpaque(false);

        addTransactionButton.addActionListener(l -> {
            JDialog addMenu = new AddTransactionDialog();
            addMenu.setVisible(true);
            addMenu.setLocationRelativeTo(App.getInstance().getFrame());
        });
        return addTransactionButton;
    }

    private JTextField getSearchField() {
        JTextField searchField = new JTextField();
        searchField.setForeground(Color.GRAY);
        searchField.setText("Search...");
        searchField.setPreferredSize(new Dimension(400, 25));
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search...")) {
                    searchField.setForeground(Color.BLACK);
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search...");
                }
            }
        });
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!(searchField.getForeground().equals(Color.GRAY))) {
                    search(searchField.getText());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!(searchField.getForeground().equals(Color.GRAY))) {
                    search(searchField.getText());
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!(searchField.getForeground().equals(Color.GRAY))) {
                    search(searchField.getText());
                }
            }
        });

        return searchField;
    }

    @Override
    public Container getContentPane() {
        return mainPanel;
    }

    @Override
    public void update() {
        DecimalFormat df = new DecimalFormat("#,###.00");
        balanceDisplay.setText("Balance: " + df.format(LoginUser.getLoggedInUser().updateBalance()));
        resetTableValues();
    }

    public void resetTableValues() {
        transactionTableModel.setDataVector(new Object[][]{}, new String[]{
                "Company",
                "Amount",
                "Date",
                "Reoccurring"
        }); //Clear the table
        for (Transaction t : LoginUser.getLoggedInUser().getTransactions()) {
            transactionTableModel.addRow(t.toArray());
        }
    }

    private void search(String text) {
        if (text.trim().isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }
}