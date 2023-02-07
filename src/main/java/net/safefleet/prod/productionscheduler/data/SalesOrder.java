package net.safefleet.prod.productionscheduler.data;

import java.util.List;

public class SalesOrder {
    private String salesOrderID;
    private List<Parts> parts;

    public SalesOrder(String salesOrderID) {
        this.salesOrderID = salesOrderID;
    }

    public String getSalesOrderID() {
        return salesOrderID;
    }

    public void setSalesOrderID(String salesOrderID) {
        this.salesOrderID = salesOrderID;
    }

    public List<Parts> getParts() {
        return parts;
    }

    public void setParts(List<Parts> parts) {
        this.parts = parts;
    }

    public static class Parts {
        private String id;
        private int quantities;
        private String dueDate;

        public Parts(String id, int quantities, String dueDate) {
            this.id = id;
            this.quantities = quantities;
            this.dueDate = dueDate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getQuantities() {
            return quantities;
        }

        public void setQuantities(int quantities) {
            this.quantities = quantities;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }
    }
}
