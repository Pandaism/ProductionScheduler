package net.safefleet.prod.productionscheduler.controllers;

import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        monSOCol.setCellFactory(param -> new SaleOrderCell());
        monSOCol.maxWidthProperty().bind(this.firstTable.widthProperty().multiply(0.3));
        monSOCol.minWidthProperty().bind(this.firstTable.widthProperty().multiply(0.3));
        monSOCol.setCellValueFactory(new PropertyValueFactory<>("salesOrderID"));
        TableColumn monPIDCol = new TableColumn("Parts");
        monPIDCol.setCellFactory(param -> new PIDCell());
        TableColumn monQuantityCol = new TableColumn("Qty");
        monQuantityCol.maxWidthProperty().bind(this.firstTable.widthProperty().multiply(0.2));
        monQuantityCol.minWidthProperty().bind(this.firstTable.widthProperty().multiply(0.2));
        monQuantityCol.setCellFactory(param -> new QuantityCell());
        this.firstCol.getColumns().addAll(monSOCol, monPIDCol, monQuantityCol);

        TableColumn tuesSOCol = new TableColumn("SO");
        tuesSOCol.setCellFactory(param -> new SaleOrderCell());
        tuesSOCol.maxWidthProperty().bind(this.firstTable.widthProperty().multiply(0.3));
        tuesSOCol.minWidthProperty().bind(this.firstTable.widthProperty().multiply(0.3));
        tuesSOCol.setCellValueFactory(new PropertyValueFactory<>("salesOrderID"));
        TableColumn tuesPIDCol = new TableColumn("Parts");
        tuesPIDCol.setCellFactory(param -> new PIDCell());
        TableColumn tuesQuantityCol = new TableColumn("Qty");
        tuesQuantityCol.maxWidthProperty().bind(this.firstTable.widthProperty().multiply(0.2));
        tuesQuantityCol.minWidthProperty().bind(this.firstTable.widthProperty().multiply(0.2));
        tuesQuantityCol.setCellFactory(param -> new QuantityCell());
        this.secondCol.getColumns().addAll(tuesSOCol,tuesPIDCol, tuesQuantityCol);

        TableColumn wedSOCol = new TableColumn("SO");
        wedSOCol.setCellFactory(param -> new SaleOrderCell());
        wedSOCol.maxWidthProperty().bind(this.firstTable.widthProperty().multiply(0.3));
        wedSOCol.minWidthProperty().bind(this.firstTable.widthProperty().multiply(0.3));
        wedSOCol.setCellValueFactory(new PropertyValueFactory<>("salesOrderID"));
        TableColumn wedPIDCol = new TableColumn("Parts");
        wedPIDCol.setCellFactory(param -> new PIDCell());
        TableColumn wedQuantityCol = new TableColumn("Qty");
        wedQuantityCol.maxWidthProperty().bind(this.firstTable.widthProperty().multiply(0.2));
        wedQuantityCol.minWidthProperty().bind(this.firstTable.widthProperty().multiply(0.2));
        wedQuantityCol.setCellFactory(param -> new QuantityCell());
        this.thirdCol.getColumns().addAll(wedSOCol,wedPIDCol, wedQuantityCol);

        TableColumn thursSOCol = new TableColumn("SO");
        thursSOCol.setCellFactory(param -> new SaleOrderCell());
        thursSOCol.maxWidthProperty().bind(this.firstTable.widthProperty().multiply(0.3));
        thursSOCol.minWidthProperty().bind(this.firstTable.widthProperty().multiply(0.3));
        thursSOCol.setCellValueFactory(new PropertyValueFactory<>("salesOrderID"));
        TableColumn thursPIDCol = new TableColumn("Parts");
        thursPIDCol.setCellFactory(param -> new PIDCell());
        TableColumn thursQuantityCol = new TableColumn("Qty");
        thursQuantityCol.maxWidthProperty().bind(this.firstTable.widthProperty().multiply(0.2));
        thursQuantityCol.minWidthProperty().bind(this.firstTable.widthProperty().multiply(0.2));
        thursQuantityCol.setCellFactory(param -> new QuantityCell());
        this.fourthCol.getColumns().addAll(thursSOCol,thursPIDCol, thursQuantityCol);

        TableColumn friSOCol = new TableColumn("SO");
        friSOCol.setCellFactory(param -> new SaleOrderCell());
        friSOCol.maxWidthProperty().bind(this.firstTable.widthProperty().multiply(0.3));
        friSOCol.minWidthProperty().bind(this.firstTable.widthProperty().multiply(0.3));
        friSOCol.setCellValueFactory(new PropertyValueFactory<>("salesOrderID"));
        TableColumn friPIDCol = new TableColumn("Parts");
        friPIDCol.setCellFactory(param -> new PIDCell());
        TableColumn friQuantityCol = new TableColumn("Qty");
        friQuantityCol.maxWidthProperty().bind(this.firstTable.widthProperty().multiply(0.2));
        friQuantityCol.minWidthProperty().bind(this.firstTable.widthProperty().multiply(0.2));
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

    private static class SaleOrderCell extends TableCell<SalesOrder, String> {
        private final Label label;

        public SaleOrderCell() {
            this.label = new Label();
            this.label.setFont(Font.font(24));
            this.setGraphic(this.label);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if(item != null || !empty) {
                if(item != null) {
                    this.label.setText(item.substring(2));
                }
            } else {
                this.label.setText(null);
            }
        }
    }

    private static class QuantityCell extends TableCell<SalesOrder, Integer> {
        private final Label label;

        public QuantityCell() {
            this.label = new Label();
            this.label.setFont(Font.font(23));
            this.setGraphic(this.label);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            int index = getIndex();
            if (index >= 0 && index < getTableView().getItems().size()) {
                SalesOrder so = getTableView().getItems().get(index);
                List<Integer> quantities = so.getParts().stream().map(SalesOrder.Parts::getQuantities).collect(Collectors.toList());
                this.label.setText(quantities.stream().map(Object::toString).collect(Collectors.joining("\n")));
            } else {
                this.label.setText(null);
            }
        }
    }

    private static class PIDCell extends TableCell<SalesOrder, String> {
        private final Label label;

        public PIDCell() {
            this.label = new Label();
            this.label.setFont(Font.font(23));
            this.setGraphic(this.label);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            int index = getIndex();
            if (index >= 0 && index < getTableView().getItems().size()) {
                SalesOrder so = getTableView().getItems().get(index);
                List<String> pids = so.getParts().stream().map(parts -> parts.getId().trim()).collect(Collectors.toList());
                this.label.setText(String.join("\n", pids));
            } else {
                this.label.setText(null);
            }


        }
    }
}