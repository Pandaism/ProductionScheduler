package net.safefleet.prod.productionscheduler.data;

import java.util.List;

/**
 * SalesOrder is a class representing a sales order with a list of parts.
 */
public class SalesOrder {
    // Declare variables to store sales order (SO) information
    private String so;
    private String dueDate;
    private List<Parts> partsList;

    /**
     * Constructor for the SalesOrder class.
     * @param so The sales order number.
     * @param dueDate The due date of the sales order.
     */
    public SalesOrder(String so, String dueDate) {
        this.so = so;
        this.dueDate = dueDate;
    }

    // Getter method for the sales order (SO) number
    public String getSo() {
        return so;
    }

    // Getter method for the due date of the sales order
    public String getDueDate() {
        return dueDate;
    }

    /**
     * Setter method for the list of parts associated with the sales order.
     * @param partsList A list of Parts objects.
     */
    public void setPartsList(List<Parts> partsList) {
        this.partsList = partsList;
    }

    // Getter method for the list of parts associated with the sales order
    public List<Parts> getPartsList() {
        return partsList;
    }

    /**
     * Parts is an inner class representing parts within a sales order.
     */
    public static class Parts {
        // Declare variables to store part information
        private String pid;
        private int quantity;

        /**
         * Constructor for the Parts class.
         * @param pid The part ID.
         * @param quantity The quantity of the part in the sales order.
         */
        public Parts(String pid, int quantity) {
            this.pid = pid;
            this.quantity = quantity;
        }

        // Getter method for the part ID
        public String getPid() {
            return pid;
        }

        // Getter method for the quantity of the part in the sales order
        public int getQuantity() {
            return quantity;
        }
    }
}
