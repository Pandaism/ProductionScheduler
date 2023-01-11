package net.safefleet.prod.productionscheduler.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import net.safefleet.prod.productionscheduler.data.SalesOrder;
import net.safefleet.prod.productionscheduler.thread.AutoScrollingThread;
import net.safefleet.prod.productionscheduler.thread.DateUpdaterThread;

import java.time.DayOfWeek;
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

    public TableView<SalesOrder> mondayTable;
    public TableView<SalesOrder> tuesdayTable;
    public TableView<SalesOrder> wednesdayTable;
    public TableView<SalesOrder> thursdayTable;
    public TableView<SalesOrder> fridayTable;

    public TableColumn<SalesOrder, String> mondayCol;
    public TableColumn<SalesOrder, String> tuesdayCol;
    public TableColumn<SalesOrder, String> wednesdayCol;
    public TableColumn<SalesOrder, String> thursdayCol;
    public TableColumn<SalesOrder, String> fridayCol;
    public ScrollPane mondayScrollbar;
    public ScrollPane tuesdayScrollbar;
    public ScrollPane wednesdayScrollbar;
    public ScrollPane thursdayScrollbar;
    public ScrollPane fridayScrollbar;

    public void initialize() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        executorService.scheduleAtFixedRate(new DateUpdaterThread(this.headerText), 0, 1, TimeUnit.DAYS);
        executorService.scheduleAtFixedRate(new AutoScrollingThread(this.mondayScrollbar, this.tuesdayScrollbar, this.wednesdayScrollbar, this.thursdayScrollbar, this.fridayScrollbar), 0, 5, TimeUnit.SECONDS);

        this.mondayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.tuesdayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.wednesdayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.thursdayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.fridayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        this.mondayCol.getColumns().addAll(monSOCol, monPIDCol, monQuantityCol);

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
        tuesQuantityCol.setCellFactory(param -> new TableCell<SalesOrder, Integer>() {
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
        this.tuesdayCol.getColumns().addAll(tuesSOCol,tuesPIDCol, tuesQuantityCol);

        TableColumn wedSOCol = new TableColumn("SO");
        TableColumn wedPIDCol = new TableColumn("PID");
        wedPIDCol.setCellFactory(param -> new TableCell<SalesOrder, String>() {
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
        TableColumn wedQuantityCol = new TableColumn("Quantity");
        wedQuantityCol.setCellFactory(param -> new TableCell<SalesOrder, Integer>() {
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
        this.wednesdayCol.getColumns().addAll(wedSOCol, wedPIDCol, wedQuantityCol);

        TableColumn thursSOCol = new TableColumn("SO");
        TableColumn thursPIDCol = new TableColumn("PID");
        thursPIDCol.setCellFactory(param -> new TableCell<SalesOrder, String>() {
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
        TableColumn thursQuantityCol = new TableColumn("Quantity");
        thursQuantityCol.setCellFactory(param -> new TableCell<SalesOrder, Integer>() {
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
        this.thursdayCol.getColumns().addAll(thursSOCol, thursPIDCol, thursQuantityCol);

        TableColumn friSOCol = new TableColumn("SO");
        TableColumn friPIDCol = new TableColumn("PID");
        friPIDCol.setCellFactory(param -> new TableCell<SalesOrder, String>() {
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
        TableColumn friQuantityCol = new TableColumn("Quantity");
        friQuantityCol.setCellFactory(param -> new TableCell<SalesOrder, Integer>() {
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
        this.fridayCol.getColumns().addAll(friSOCol, friPIDCol, friQuantityCol);

// Check the due date and add the SalesOrder to the appropriate TableColumn
        List<SalesOrder> monday = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            monday.add(
                    new SalesOrder("SO-" + i, Arrays.asList(
                            new SalesOrder.Parts("FOCUS-11-00", new Random().nextInt(20) + 1),
                            new SalesOrder.Parts("FOCUS-51-00", new Random().nextInt(10) + 1)),
                            LocalDate.of(2023, 1, 9)));
        }

        List<SalesOrder> tuesday = new ArrayList<>();
        for(int i = 21; i < 50; i++) {
            tuesday.add(
                    new SalesOrder("SO-" + i, Arrays.asList(
                            new SalesOrder.Parts("FOCUS-11-00", new Random().nextInt(20) + 1),
                            new SalesOrder.Parts("FOCUS-51-00", new Random().nextInt(10) + 1)),
                            LocalDate.of(2023, 1, 10)));
        }
        List<SalesOrder> wednesday = new ArrayList<>();
        for(int i = 51; i < 55; i++) {
            wednesday.add(
                    new SalesOrder("SO-" + i, Arrays.asList(
                            new SalesOrder.Parts("FOCUS-11-00", new Random().nextInt(20) + 1),
                            new SalesOrder.Parts("FOCUS-51-00", new Random().nextInt(10) + 1)),
                            LocalDate.of(2023, 1, 11)));
        }

        List<SalesOrder> thursday = new ArrayList<>();
        for(int i = 55; i < 58; i++) {
            thursday.add(
                    new SalesOrder("SO-" + i, Arrays.asList(
                            new SalesOrder.Parts("FOCUS-11-00", new Random().nextInt(20) + 1),
                            new SalesOrder.Parts("FOCUS-51-00", new Random().nextInt(10) + 1)),
                            LocalDate.of(2023, 1, 12)));
        }

        List<SalesOrder> salesList = new ArrayList<>();
        salesList.addAll(monday);
        salesList.addAll(tuesday);
        salesList.addAll(wednesday);
        salesList.addAll(thursday);

        for(SalesOrder salesOrder : salesList) {
            switch (salesOrder.getDueDate().getDayOfWeek()) {
                case MONDAY:
                    this.mondayTable.getItems().add(salesOrder);
                    break;
                case TUESDAY:
                    this.tuesdayTable.getItems().add(salesOrder);
                    break;
                case WEDNESDAY:
                    this.wednesdayTable.getItems().add(salesOrder);
                    break;
                case THURSDAY:
                    this.thursdayTable.getItems().add(salesOrder);
                    break;
                case FRIDAY:
                    this.fridayTable.getItems().add(salesOrder);
                    break;
                default:

            }
        }
    }
}

/*


ObservableList<SalesOrder> salesOrders = FXCollections.observableArrayList(
                new SalesOrder("SO000001", LocalDate.of(2023, 1,16)),
                new SalesOrder("SO3000403", LocalDate.of(2023, 1,17)),
                new SalesOrder("SO5555", LocalDate.of(2023, 1,20)),
                new SalesOrder("SO5556", LocalDate.of(2023, 1,20)),
                new SalesOrder("SO5557", LocalDate.of(2023, 1,20)),
                new SalesOrder("SO5558", LocalDate.of(2023, 1,20)),
                new SalesOrder("SO5559", LocalDate.of(2023, 1,20)),
                new SalesOrder("SO5560", LocalDate.of(2023, 1,20))
        );
 */