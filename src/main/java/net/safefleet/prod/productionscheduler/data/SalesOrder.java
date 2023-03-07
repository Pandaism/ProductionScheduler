package net.safefleet.prod.productionscheduler.data;

import java.util.List;

public class SalesOrder {
    private final String salesOrderID;
    private List<Parts> parts;

    public SalesOrder(String salesOrderID) {
        this.salesOrderID = salesOrderID;
    }

    public String getSalesOrderID() {
        return salesOrderID;
    }

    public List<Parts> getParts() {
        return parts;
    }

    public void setParts(List<Parts> parts) {
        this.parts = parts;
    }

    public static class Parts {
        private final String id;
        private final int quantities;
        private final String dueDate;

        public Parts(String id, int quantities, String dueDate) {
            this.id = id;
            this.quantities = quantities;
            this.dueDate = dueDate;
        }

        public String getId() {
            return id;
        }

        public int getQuantities() {
            return quantities;
        }

        public String getDueDate() {
            return dueDate;
        }

    }
}
