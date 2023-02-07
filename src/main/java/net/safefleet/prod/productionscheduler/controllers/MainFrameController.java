package net.safefleet.prod.productionscheduler.controllers;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.safefleet.prod.productionscheduler.data.SalesOrder;
import net.safefleet.prod.productionscheduler.thread.AutoScrollingThread;
import net.safefleet.prod.productionscheduler.thread.DateUpdaterThread;
import net.safefleet.prod.productionscheduler.thread.ExpandableReaderThread;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MainFrameController {
    public Label headerText;

    public TableView<SalesOrder> firstTable;
    public TableView<SalesOrder> secondTable;
    public TableView<SalesOrder> thirdTable;
    public TableView<SalesOrder> fourthTable;
    public TableView<SalesOrder> fifthTable;

    public TableColumn<SalesOrder, String> firstCol;
    public TableColumn<SalesOrder, String> secondCol;
    public TableColumn<SalesOrder, String> thirdCol;
    public TableColumn<SalesOrder, String> fourthCol;
    public TableColumn<SalesOrder, String> fifthCol;

    public ScrollPane firstScrollbar;
    public ScrollPane secondScrollbar;
    public ScrollPane thirdScrollbar;
    public ScrollPane fourthScrollbar;
    public ScrollPane fifthScrollbar;

    public void initialize() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        executorService.scheduleAtFixedRate(new DateUpdaterThread(this.headerText), 0, 1, TimeUnit.DAYS);
        executorService.scheduleAtFixedRate(new AutoScrollingThread(this.firstScrollbar, this.secondScrollbar, this.thirdScrollbar, this.fourthScrollbar, this.fifthScrollbar), 0, 5, TimeUnit.SECONDS);

        this.firstTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.secondTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.thirdTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.fourthTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.fifthTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn monSOCol = new TableColumn("SO");
        monSOCol.setCellValueFactory(new PropertyValueFactory<>("salesOrderID"));

        TableColumn monPIDCol = new TableColumn("Parts");
        monPIDCol.setCellFactory(param -> new PIDCell());
        TableColumn monQuantityCol = new TableColumn("Quantity");
        monQuantityCol.setCellFactory(param -> new QuantityCell());

        this.firstCol.getColumns().addAll(monSOCol, monPIDCol, monQuantityCol);

        TableColumn tuesSOCol = new TableColumn("SO");
        tuesSOCol.setCellValueFactory(new PropertyValueFactory<>("salesOrderID"));
        TableColumn tuesPIDCol = new TableColumn("Parts");
        tuesPIDCol.setCellFactory(param -> new PIDCell());
        TableColumn tuesQuantityCol = new TableColumn("Quantity");
        tuesQuantityCol.setCellFactory(param -> new QuantityCell());
        this.secondCol.getColumns().addAll(tuesSOCol,tuesPIDCol, tuesQuantityCol);

        TableColumn wedSOCol = new TableColumn("SO");
        wedSOCol.setCellValueFactory(new PropertyValueFactory<>("salesOrderID"));
        TableColumn wedPIDCol = new TableColumn("Parts");
        wedPIDCol.setCellFactory(param -> new PIDCell());
        TableColumn wedQuantityCol = new TableColumn("Quantity");
        wedQuantityCol.setCellFactory(param -> new QuantityCell());
        this.thirdCol.getColumns().addAll(wedSOCol,wedPIDCol, wedQuantityCol);

        TableColumn thursSOCol = new TableColumn("SO");
        thursSOCol.setCellValueFactory(new PropertyValueFactory<>("salesOrderID"));
        TableColumn thursPIDCol = new TableColumn("Parts");
        thursPIDCol.setCellFactory(param -> new PIDCell());
        TableColumn thursQuantityCol = new TableColumn("Quantity");
        thursQuantityCol.setCellFactory(param -> new QuantityCell());
        this.fourthCol.getColumns().addAll(thursSOCol,thursPIDCol, thursQuantityCol);

        TableColumn friSOCol = new TableColumn("SO");
        friSOCol.setCellValueFactory(new PropertyValueFactory<>("salesOrderID"));
        TableColumn friPIDCol = new TableColumn("Parts");
        friPIDCol.setCellFactory(param -> new PIDCell());
        TableColumn friQuantityCol = new TableColumn("Quantity");
        friQuantityCol.setCellFactory(param -> new QuantityCell());
        this.fifthCol.getColumns().addAll(friSOCol,friPIDCol, friQuantityCol);

        executorService.scheduleAtFixedRate(
                new ExpandableReaderThread(
                        new ExpandableReaderThread.TableContainer(this.firstTable, this.firstCol),
                        new ExpandableReaderThread.TableContainer(this.secondTable, this.secondCol),
                        new ExpandableReaderThread.TableContainer(this.thirdTable, this.thirdCol),
                        new ExpandableReaderThread.TableContainer(this.fourthTable, this.fourthCol),
                        new ExpandableReaderThread.TableContainer(this.fifthTable, this.fifthCol)
                ), 0, 30, TimeUnit.MINUTES);
    }

    private static class QuantityCell extends TableCell<SalesOrder, Integer> {
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
    }

    private static class PIDCell extends TableCell<SalesOrder, String> {
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
    }
}