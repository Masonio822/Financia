package gui.table;

import data.Transaction;
import data.user.LoginUser;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Arrays;

public class TransactionTableModel implements TableModel {
    private final String[] columnNames;

    private Object[][] data;

    public TransactionTableModel() {
        data = new Object[][]{LoginUser.getLoggedInUser().getTransactions()
                .stream()
                .map(Transaction::toArray)
                .toArray()

        };
        if (Arrays.deepEquals(data, new Object[][]{new Object[]{}})) {
            data = new Object[][]{};
        }

        columnNames = new String[] {
                "Company",
                "Amount",
                "Date",
                "Reoccurring"
        };
    }

    @Override
    public int getRowCount() {
            return data.length;
        }

    @Override
    public int getColumnCount() {
            return columnNames.length;
        }

    @Override
    public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }

    public static void updateTable() {
    }
}
