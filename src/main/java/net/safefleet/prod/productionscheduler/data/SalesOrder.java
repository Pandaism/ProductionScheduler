package net.safefleet.prod.productionscheduler.data;

import java.util.List;

public class SalesOrder {
    private String so;
    private String dueDate;
    private List<Parts> partsList;

    public SalesOrder(String so, String dueDate) {
        this.so = so;
        this.dueDate = dueDate;
    }

    public String getSo() {
        return so;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setPartsList(List<Parts> partsList) {
        this.partsList = partsList;
    }

    public List<Parts> getPartsList() {
        return partsList;
    }

    public static class Parts {
        private String pid;
        private int quantity;

        public Parts(String pid, int quantity) {
            this.pid = pid;
            this.quantity = quantity;
        }

        public String getPid() {
            return pid;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
