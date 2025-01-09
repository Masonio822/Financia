package gui.observer;

import javax.swing.*;
import javax.swing.table.TableModel;

public interface Observer {
    void update();
    void filter(RowFilter<? super TableModel,? super Integer> filter);
}
