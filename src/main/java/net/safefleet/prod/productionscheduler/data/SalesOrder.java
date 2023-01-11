package net.safefleet.prod.productionscheduler.data;

import java.time.LocalDate;
import java.util.List;

public class SalesOrder {
    private String salesOrderNumber;
    private List<Parts> parts;
    private LocalDate dueDate;

    public SalesOrder(String salesOrderNumber, List<Parts> parts, LocalDate dueDate) {
        this.salesOrderNumber = salesOrderNumber;
        this.parts = parts;
        this.dueDate = dueDate;
    }

    public String getSalesOrderNumber() {
        return salesOrderNumber;
    }

    public void setSalesOrderNumber(String salesOrderNumber) {
        this.salesOrderNumber = salesOrderNumber;
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
