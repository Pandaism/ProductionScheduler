package net.safefleet.prod.productionscheduler.thread;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.safefleet.prod.productionscheduler.data.SalesOrder;
import net.safefleet.prod.productionscheduler.data.database.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class ExpandableReaderThread implements Runnable {
    private final TableContainer[] containers;
    private final Map<String, List<SalesOrder.Parts>> orderMap = new HashMap<>();

    public ExpandableReaderThread(TableContainer... containers) {
        this.containers = containers;
    }

    @Override
    public void run() {
        System.out.println("Updating tables");
        this.orderMap.clear();
        for(TableContainer tableContainer : this.containers) {
            tableContainer.getTableView().getItems().clear();
        }

        Map<String, List<SalesOrder.Parts>> cobanMap = new Query(Query.CORP.COBAN).queryDB();
        Map<String, List<SalesOrder.Parts>> fleetmindMap = new Query(Query.CORP.FLEETMIND).queryDB();

        for (Map.Entry<String, List<SalesOrder.Parts>> entry : cobanMap.entrySet()) {
            this.orderMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        for (Map.Entry<String, List<SalesOrder.Parts>> entry : fleetmindMap.entrySet()) {
            String key = entry.getKey();
            List<SalesOrder.Parts> value = entry.getValue();
            if (this.orderMap.containsKey(key)) {
                this.orderMap.get(key).addAll(value);
            } else {
                this.orderMap.put(key, value);
            }
        }

        try {
            for(String key : this.orderMap.keySet()) {
                SalesOrder salesOrder = new SalesOrder(key);
                salesOrder.setParts(this.orderMap.get(key));

                for(SalesOrder.Parts part : salesOrder.getParts()) {
                    DateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0");
                    Date sqlDate = sqlFormat.parse(part.getDueDate());
                    DateFormat dayOfWeek = new SimpleDateFormat("EEEE");
                    String dayOfWeekDate = dayOfWeek.format(sqlDate);

                    for(TableContainer container : this.containers) {
                        if(dayOfWeekDate.equals(container.getAssociatedColumn().getText())) {
                            TableView<SalesOrder> table = container.getTableView();
                            if(!table.getItems().contains(salesOrder)) {
                                container.getTableView().getItems().add(salesOrder);
                            }
                        }
                    }
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static class TableContainer {
        private final TableView<SalesOrder> tableView;
        private final TableColumn<SalesOrder, String> associatedColumn;

        public TableContainer(TableView<SalesOrder> tableView, TableColumn<SalesOrder, String> associatedColumn) {
            this.tableView = tableView;
            this.associatedColumn = associatedColumn;
        }

        public TableView<SalesOrder> getTableView() {
            return tableView;
        }

        public TableColumn<SalesOrder, String> getAssociatedColumn() {
            return associatedColumn;
        }
    }
}
