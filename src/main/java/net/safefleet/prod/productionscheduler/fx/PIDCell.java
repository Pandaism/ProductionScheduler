package net.safefleet.prod.productionscheduler.fx;

import net.safefleet.prod.productionscheduler.data.SalesOrder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PIDCell is a custom TableCell class for displaying part IDs (pids)
 * from a SalesOrder object. This class extends FormattedTableCell and
 * provides a formatted display of part IDs in a TableView.
 */
public class PIDCell extends FormattedTableCell<SalesOrder, String> {
    /**
     * Override the updateItem method to handle the SalesOrder object.
     *
     * @param item The part ID (pid) string for the current cell.
     * @param empty Indicates whether the cell is empty or not.
     */
    @Override
    protected void updateItem(String item, boolean empty) {
        // Call the superclass updateItem method
        super.updateItem(item, empty);

        // Get the index of the current cell
        int index = getIndex();

        // Check if the index is within the range of the items in the TableView
        if (index >= 0 && index < getTableView().getItems().size()) {
            // Get the SalesOrder object for the current row
            SalesOrder so = getTableView().getItems().get(index);

            // Extract the list of part IDs (pids) from the SalesOrder
            List<String> pids = so.getPartsList().stream().map(parts -> parts.getPid().trim()).collect(Collectors.toList());

            // Call the setFormattedText method to display the part IDs in the TableCell
            setFormattedText(pids, this.getTableColumn());
        } else {
            // If the cell is empty, clear the text
            super.text.setText(null);
        }
    }
}
