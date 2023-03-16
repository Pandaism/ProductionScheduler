package net.safefleet.prod.productionscheduler.fx;

import net.safefleet.prod.productionscheduler.data.SalesOrder;

import java.util.Collections;

/**
 * SaleOrderCell is a custom TableCell class for displaying sales order numbers
 * from a SalesOrder object. This class extends FormattedTableCell and provides
 * a formatted display of sales order numbers in a TableView.
 */
public class SaleOrderCell extends FormattedTableCell<SalesOrder, String> {
    /**
     * Override the updateItem method to handle the SalesOrder object.
     *
     * @param item The sales order number for the current cell.
     * @param empty Indicates whether the cell is empty or not.
     */
    @Override
    protected void updateItem(String item, boolean empty) {
        // Call the superclass updateItem method
        super.updateItem(item, empty);

        // Check if the item is not null and not empty
        if (item != null && !empty) {
            // If the item contains "SO", remove the "SO" prefix before setting the formatted text
            if (item.contains("SO")) {
                setFormattedText(Collections.singletonList(item.substring(2)), this.getTableColumn());
            } else {
                // Otherwise, use the item as is for setting the formatted text
                setFormattedText(Collections.singletonList(item), this.getTableColumn());
            }
        } else {
            // If the cell is empty, clear the text
            this.text.setText(null);
        }
    }

}
