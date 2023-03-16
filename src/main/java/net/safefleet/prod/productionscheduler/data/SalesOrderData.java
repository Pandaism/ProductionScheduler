package net.safefleet.prod.productionscheduler.data;

/**
 * SalesOrderData is a class representing a sales order data entry
 * containing sales order ID, part ID, quantities, and due date.
 */
public class SalesOrderData {
    // Declare variables to store sales order data
    private final String salesOrderID;
    private final String pid;
    private final int quantities;
    private final String dueDate;

    /**
     * Constructor for the SalesOrderData class.
     * @param salesOrderID The sales order ID.
     * @param pid The part ID.
     * @param quantities The quantities of the part in the sales order.
     * @param dueDate The due date of the sales order.
     */
    public SalesOrderData(String salesOrderID, String pid, int quantities, String dueDate) {
        this.salesOrderID = salesOrderID;
        this.pid = pid;
        this.quantities = quantities;
        this.dueDate = dueDate;
    }

    // Getter method for the sales order ID
    public String getSalesOrderID() {
        return salesOrderID;
    }

    // Getter method for the part ID
    public String getPid() {
        return pid;
    }

    // Getter method for the quantities of the part in the sales order
    public int getQuantities() {
        return quantities;
    }

    // Getter method for the due date of the sales order
    public String getDueDate() {
        return dueDate;
    }
}
