package net.safefleet.prod.productionscheduler.thread;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import net.safefleet.prod.productionscheduler.data.SalesOrder;
import net.safefleet.prod.productionscheduler.data.SalesOrderData;
import net.safefleet.prod.productionscheduler.data.database.Query;
import net.safefleet.prod.productionscheduler.fx.PIDCell;
import net.safefleet.prod.productionscheduler.fx.QuantityCell;
import net.safefleet.prod.productionscheduler.fx.SaleOrderCell;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExpandableReaderThread implements Runnable {
    private final ScrollPane[] containers;
    private final HBox centerBox;

    public ExpandableReaderThread(HBox centerBox, ScrollPane... containers) {
        this.centerBox = centerBox;
        this.containers = containers;
    }

    @Override
    public void run() {
        System.out.println("Updating tables");
        Platform.runLater(() -> {
            this.centerBox.getChildren().clear();
            for(ScrollPane scrollPane : this.containers) {
                scrollPane.setContent(null);
            }
        });

        List<SalesOrderData> dataList = new ArrayList<>();
        Set<String> dayOfWeeks = new TreeSet<>();

        for(Query.CORP corp : Query.CORP.values()) {
            Query query = new Query(corp);
            dataList.addAll(query.queryDB());
            dayOfWeeks.addAll(query.getDayOfWeeks());
        }

        if(dataList.size() > 0) {
            List<SalesOrder> salesOrderList = new ArrayList<>();
            SalesOrder currentSalesOrder = null;
            List<SalesOrder.Parts> currentPartsList = null;
            for (SalesOrderData salesOrderData : dataList) {
                if (currentSalesOrder == null || !currentSalesOrder.getSo().equals(salesOrderData.getSalesOrderID())) {
                    currentSalesOrder = new SalesOrder(salesOrderData.getSalesOrderID(), salesOrderData.getDueDate());
                    currentPartsList = new ArrayList<>();
                    currentSalesOrder.setPartsList(currentPartsList);
                    salesOrderList.add(currentSalesOrder);
                }
                SalesOrder.Parts parts = new SalesOrder.Parts(salesOrderData.getPid(), salesOrderData.getQuantities());
                currentPartsList.add(parts);
            }

            DateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0");
            DateFormat dayOfWeekdf = new SimpleDateFormat("EEEE");
            Map<String, TableView<SalesOrder>> tables = new HashMap<>();

            int counter = 0;
            for (String dayOfWeek : dayOfWeeks) {
                int finalCounter = counter;
                Platform.runLater(() -> {
                    try {
                        TableView<SalesOrder> table = new TableView<>();
                        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                        Date sqlDate = sqlFormat.parse(dayOfWeek);
                        String dayOfWeekDate = dayOfWeekdf.format(sqlDate);
                        TableColumn dateCol = new TableColumn(dayOfWeekDate);

                        TableColumn soCol = new TableColumn("SO");
                        soCol.setCellFactory(param -> new SaleOrderCell());
                        soCol.setCellValueFactory(new PropertyValueFactory<>("so"));
                        soCol.maxWidthProperty().bind(table.widthProperty().multiply(0.3));
                        soCol.minWidthProperty().bind(table.widthProperty().multiply(0.3));

                        TableColumn pidCol = new TableColumn("PID");
                        pidCol.setCellFactory(param -> new PIDCell());

                        TableColumn quantityCol = new TableColumn("Qty");
                        quantityCol.setCellFactory(param -> new QuantityCell());
                        quantityCol.maxWidthProperty().bind(table.widthProperty().multiply(0.2));
                        quantityCol.minWidthProperty().bind(table.widthProperty().multiply(0.2));

                        dateCol.getColumns().add(soCol);
                        dateCol.getColumns().add(pidCol);
                        dateCol.getColumns().add(quantityCol);
                        table.getColumns().add(dateCol);
                        this.containers[finalCounter].setContent(table);
                        this.centerBox.getChildren().add(this.containers[finalCounter]);
                        tables.put(dayOfWeekDate, table);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                });
                counter++;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(SalesOrder salesOrder : salesOrderList) {
                try {
                    Date sqlDate = sqlFormat.parse(salesOrder.getDueDate());
                    String dayOfWeekDate = dayOfWeekdf.format(sqlDate);

                    Platform.runLater(() -> {
                        TableView<SalesOrder> tableView = tables.get(dayOfWeekDate);
                        tableView.getItems().add(salesOrder);
                    });

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
