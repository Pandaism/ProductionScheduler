package net.safefleet.prod.productionscheduler.fx;

import javafx.scene.control.TableCell;
import net.safefleet.prod.productionscheduler.data.SalesOrder;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class SalesOrderCell extends TableCell<SalesOrder, LocalDate> {
    @Override
    protected void updateItem(LocalDate dueDate, boolean empty) {
        super.updateItem(dueDate, empty);
        if (empty || dueDate == null) {
            setText(null);
        } else {
            int columnIndex = getTableColumn().getTableView().getColumns().indexOf(getTableColumn()) + 1;
            DayOfWeek columnDayOfWeek = DayOfWeek.of(columnIndex);
            if (dueDate.getDayOfWeek().equals(columnDayOfWeek)) {
                setText(getTableRow().getItem().getSalesOrderID());
            } else {
                setText(null);
            }
        }
    }
}
