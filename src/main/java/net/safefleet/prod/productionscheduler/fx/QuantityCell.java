package net.safefleet.prod.productionscheduler.fx;

import net.safefleet.prod.productionscheduler.data.SalesOrder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * QuantityCell is a custom TableCell class for displaying quantities
 * of parts from a SalesOrder object. This class extends FormattedTableCell
 * and provides a formatted display of part quantities in a TableView.
 */
public class QuantityCell extends FormattedTableCell<SalesOrder, Integer> {
    /**
     * Override the updateItem method to handle the SalesOrder object.
     *
     * @param item The quantity of the part for the current cell.
     * @param empty Indicates whether the cell is empty or not.
     */
    @Override
    protected void updateItem(Integer item, boolean empty) {
        // Call the superclass updateItem method
        super.updateItem(item, empty);

        // Get the index of the current cell
        int index = getIndex();

        // Check if the index is within the range of the items in the TableView
        if (index >= 0 && index < getTableView().getItems().size()) {
            // Get the SalesOrder object for the current row
            SalesOrder so = getTableView().getItems().get(index);

            /*
                Hack around to sync the font size of PIDCell and QuantityCell
             */
            List<String> pids = so.getPartsList().stream().map(parts -> parts.getPid().trim()).collect(Collectors.toList());
            setFormattedText(pids, this.getTableView().getColumns().get(0).getColumns().get(1));

            // Extract the list of quantities from the SalesOrder
            List<Integer> quantities = so.getPartsList().stream().map(SalesOrder.Parts::getQuantity).collect(Collectors.toList());

            // Set the text of this cell to the quantities, separated by newline characters
            super.text.setText(quantities.stream().map(Object::toString).collect(Collectors.joining("\n")));

        } else {
            // If the cell is empty, clear the text
            super.text.setText(null);
        }
    }
}
