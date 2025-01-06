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
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainScreen implements Screen, Observer {
    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final JLabel balanceDisplay;
    private final DefaultTableModel transactionTableModel = new DefaultTableModel(new String[]{
        "Company",
        "Amount",
        "Date",
        "Reoccurring"
    }, 0);

    public MainScreen() {
        Notifier.addObserver(this);

        JLabel header = new JLabel("Financia");
        header.setFont(ResourceLoader.getInstance().getRighteousFont(52));
        header.setForeground(new Color(224, 159, 54));
        JLabel welcome = new JLabel("Welcome, " + LoginUser.getInstance().getBalance() + "!");
        mainPanel.add(GuiUtils.group(GuiUtils.VERTICAL, header, welcome), BorderLayout.NORTH);

        balanceDisplay = new JLabel(String.valueOf(LoginUser.getInstance().getBalance()));
        mainPanel.add(balanceDisplay, BorderLayout.NORTH);

        JScrollPane transactionList = getTable();
        mainPanel.add(transactionList, BorderLayout.CENTER);

        JButton addTransactionButton = getTransactionButton();
        JPanel p = GuiUtils.align(GuiUtils.RIGHT, addTransactionButton);
        p.setBorder(new EmptyBorder(0, 0, 25, 50));
        mainPanel.add(p, BorderLayout.SOUTH);
    }

    private JScrollPane getTable() {
        resetTableValues();
        JTable transactionTable = new JTable(transactionTableModel);
        transactionTable.setShowHorizontalLines(true);
        transactionTable.setFillsViewportHeight(true);

        JScrollPane transactionList = new JScrollPane(transactionTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        transactionList.setBorder(new EmptyBorder(100, 100, 100, 150));
        return transactionList;
    }

    private JButton getTransactionButton() {
        JButton addTransactionButton = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(200, 144, 25));
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

    @Override
    public Container getContentPane() {
        return mainPanel;
    }

    @Override
    public void update() {
        balanceDisplay.setText(String.valueOf(LoginUser.getInstance().updateBalance()));
        resetTableValues();
    }

    public void resetTableValues() {
        transactionTableModel.setDataVector(new Object[][]{}, new String[]{
                "Company",
                "Amount",
                "Date",
                "Reoccurring"
        }); //Clear the table
        for (Transaction t : LoginUser.getInstance().getTransactions()) {
            transactionTableModel.addRow(t.toArray());
        }
    }
}