package net.safefleet.prod.productionscheduler.data;

public class SalesOrderData {
    private final String salesOrderID;
    private final String pid;
    private final int quantities;
    private final String dueDate;


    public SalesOrderData(String salesOrderID, String pid, int quantities, String dueDate) {
        this.salesOrderID = salesOrderID;
        this.pid = pid;
        this.quantities = quantities;
        this.dueDate = dueDate;
    }

    public String getSalesOrderID() {
        return salesOrderID;
    }

    public String getPid() {
        return pid;
    }

    public int getQuantities() {
        return quantities;
    }

    public String getDueDate() {
        return dueDate;
    }
}
