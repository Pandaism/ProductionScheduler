package net.safefleet.prod.productionscheduler.data;

import java.time.LocalDate;
import java.util.List;

public class SalesOrder {
    private String salesOrderID;
    private String salesOrderType;
    private String salesOrderStatus;
    private String salesOrderClass;
    private List<Parts> parts;
    private LocalDate dueDate;

    public SalesOrder(String salesOrderID, List<Parts> parts, LocalDate dueDate) {
        this.salesOrderID = salesOrderID;
        this.parts = parts;
        this.dueDate = dueDate;
    }

    public String getSalesOrderID() {
        return salesOrderID;
    }

    public void setSalesOrderID(String salesOrderID) {
        this.salesOrderID = salesOrderID;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
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

        public Parts(String id, int quantities) {
            this.id = id;
            this.quantities = quantities;
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
    }
}
