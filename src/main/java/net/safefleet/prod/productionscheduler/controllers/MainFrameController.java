package net.safefleet.prod.productionscheduler.controllers;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import net.safefleet.prod.productionscheduler.data.SalesOrder;
import net.safefleet.prod.productionscheduler.thread.AutoScrollingThread;
import net.safefleet.prod.productionscheduler.thread.DateUpdaterThread;
import net.safefleet.prod.productionscheduler.thread.ExpandableReaderThread;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MainFrameController {
    public Label headerText;

    public TableView<SalesOrder> firstTable;
    public TableView<SalesOrder> secondTable;

    public TableColumn<SalesOrder, String> firstCol;
    public TableColumn<SalesOrder, String> secondCol;
    public ScrollPane firstScrollbar;
    public ScrollPane secondScrollbar;

    public void initialize() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        executorService.scheduleAtFixedRate(new DateUpdaterThread(this.headerText), 0, 1, TimeUnit.DAYS);
        executorService.scheduleAtFixedRate(new AutoScrollingThread(this.firstScrollbar, this.secondScrollbar), 0, 5, TimeUnit.SECONDS);

        this.firstTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.secondTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn monSOCol = new TableColumn("SO");
        monSOCol.setCellValueFactory(new PropertyValueFactory<>("salesOrderNumber"));

        TableColumn monPIDCol = new TableColumn("Parts");
        monPIDCol.setCellFactory(param -> new TableCell<SalesOrder, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                int index = getIndex();
                if (index >= 0 && index < getTableView().getItems().size()) {
                    SalesOrder so = getTableView().getItems().get(index);
                    List<String> pids = so.getParts().stream().map(SalesOrder.Parts::getId).collect(Collectors.toList());
                    setText(String.join("\n", pids));
                } else {
                    setText(null);
                }
            }
        });
        TableColumn monQuantityCol = new TableColumn("Quantity");
        monQuantityCol.setCellFactory(param -> new TableCell<SalesOrder, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                int index = getIndex();
                if (index >= 0 && index < getTableView().getItems().size()) {
                    SalesOrder so = getTableView().getItems().get(index);
                    List<Integer> quantities = so.getParts().stream().map(SalesOrder.Parts::getQuantities).collect(Collectors.toList());
                    setText(String.join("\n", quantities.stream().map(Object::toString).collect(Collectors.toList())));
                } else {
                    setText(null);
                }
            }
        });

        this.firstCol.getColumns().addAll(monSOCol, monPIDCol, monQuantityCol);

        TableColumn tuesSOCol = new TableColumn("SO");
        tuesSOCol.setCellValueFactory(new PropertyValueFactory<>("salesOrderNumber"));
        TableColumn tuesPIDCol = new TableColumn("Parts");
        tuesPIDCol.setCellFactory(param -> new TableCell<SalesOrder, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                int index = getIndex();
                if (index >= 0 && index < getTableView().getItems().size()) {
                    SalesOrder so = getTableView().getItems().get(index);
                    List<String> pids = so.getParts().stream().map(SalesOrder.Parts::getId).collect(Collectors.toList());
                    setText(String.join("\n", pids));
                } else {
                    setText(null);
                }
            }
        });
        TableColumn tuesQuantityCol = new TableColumn("Quantity");
        tuesQuantityCol.setCellFactory(param -> {
            return new TableCell<SalesOrder, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        SalesOrder so = getTableView().getItems().get(index);
                        List<Integer> quantities = so.getParts().stream().map(SalesOrder.Parts::getQuantities).collect(Collectors.toList());
                        setText(String.join("\n", quantities.stream().map(Object::toString).collect(Collectors.toList())));
                    } else {
                        setText(null);
                    }
                }
            };
        });
        this.secondCol.getColumns().addAll(tuesSOCol,tuesPIDCol, tuesQuantityCol);

        executorService.scheduleAtFixedRate(new ExpandableReaderThread(), 0, 30, TimeUnit.MINUTES);
// Check the due date and add the SalesOrder to the appropriate TableColumn
        List<SalesOrder> monday = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            monday.add(
                    new SalesOrder("SO-" + i, Arrays.asList(
                            new SalesOrder.Parts("FOCUS-11-00", new Random().nextInt(20) + 1),
                            new SalesOrder.Parts("FOCUS-51-00", new Random().nextInt(10) + 1)),
                            LocalDate.of(2023, 1, 18)));
        }

        for(int i = 21; i < 50; i++) {
            monday.add(
                    new SalesOrder("SO-" + i, Arrays.asList(
                            new SalesOrder.Parts("FOCUS-11-00", new Random().nextInt(20) + 1),
                            new SalesOrder.Parts("FOCUS-51-00", new Random().nextInt(10) + 1)),
                            LocalDate.of(2023, 1, 19)));
        }
        for(int i = 51; i < 55; i++) {
            monday.add(
                    new SalesOrder("SO-" + i, Arrays.asList(
                            new SalesOrder.Parts("FOCUS-11-00", new Random().nextInt(20) + 1),
                            new SalesOrder.Parts("FOCUS-51-00", new Random().nextInt(10) + 1)),
                            LocalDate.of(2023, 1, 20)));
        }


        List<SalesOrder> salesList = new ArrayList<>();
        salesList.addAll(monday);

        for(SalesOrder salesOrder : salesList) {
            if(salesOrder.getDueDate().getDayOfWeek() == LocalDate.now().getDayOfWeek()) {
                this.firstTable.getItems().add(salesOrder);
            } else if(salesOrder.getDueDate().getDayOfWeek() == LocalDate.now().getDayOfWeek().plus(1)) {
                this.secondTable.getItems().add(salesOrder);
            }
        }

        this.firstTable.getItems().clear();
    }
}