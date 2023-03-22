package net.safefleet.prod.productionscheduler.thread;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import net.safefleet.prod.productionscheduler.data.SalesOrder;
import net.safefleet.prod.productionscheduler.data.SalesOrderData;
import net.safefleet.prod.productionscheduler.data.database.Query;
import net.safefleet.prod.productionscheduler.fx.PIDCell;
import net.safefleet.prod.productionscheduler.fx.QuantityCell;
import net.safefleet.prod.productionscheduler.fx.SaleOrderCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ExpandableReaderThread class implements Runnable interface to fetch, process and display sales order data in a multi-threaded environment.
 */
public class ExpandableReaderThread implements Runnable {
    /**
     * Static inner class TableColumnBuilder is a utility class for building TableColumn with a fluent API.
     */
    private static class TableColumnBuilder {
        private TableColumn tableColumn;

        /**
         * Constructor taking the title for the TableColumn.
         *
         * @param title The title for the TableColumn.
         */
        public TableColumnBuilder(String title) {
            // Initialize the TableColumn with the given title
            this.tableColumn = new TableColumn(title);
        }

        /**
         * Sets the size (max and min width) of the TableColumn, based on the reference TableView's width.
         *
         * @param ref        The reference TableView.
         * @param multiplier The multiplier for the width calculation.
         * @return TableColumnBuilder instance for method chaining.
         */
        public TableColumnBuilder setSize(TableView<SalesOrder> ref, double multiplier) {
            // Bind the max width property of the TableColumn to the width property of the reference TableView multiplied by the given multiplier
            this.tableColumn.maxWidthProperty().bind(ref.widthProperty().multiply(multiplier));
            // Bind the min width property of the TableColumn to the width property of the reference TableView multiplied by the given multiplier
            this.tableColumn.minWidthProperty().bind(ref.widthProperty().multiply(multiplier));
            // Return this TableColumnBuilder instance for method chaining
            return this;
        }

        /**
         * Sets the cell factory of the TableColumn.
         *
         * @param value The cell factory callback.
         * @return TableColumnBuilder instance for method chaining.
         */
        public TableColumnBuilder setCellFactory(Callback<TableColumn, TableCell> value) {
            // Set the cell factory of the TableColumn
            this.tableColumn.setCellFactory(value);
            // Return this TableColumnBuilder instance for method chaining
            return this;
        }

        /**
         * Sets the cell value factory of the TableColumn.
         *
         * @param value The cell value factory callback.
         * @return TableColumnBuilder instance for method chaining.
         */
        public TableColumnBuilder setCellValueFactory(Callback<TableColumn.CellDataFeatures, ObservableValue> value) {
            // Set the cell value factory of the TableColumn
            this.tableColumn.setCellValueFactory(value);
            // Return this TableColumnBuilder instance for method chaining
            return this;
        }

        /**
         * Builds and returns the TableColumn object.
         *
         * @return The TableColumn object.
         */
        public TableColumn build() {
            // Return the TableColumn object
            return this.tableColumn;
        }

    }
    // Logger for logging information and errors
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpandableReaderThread.class);
    // Array of ScrollPanes that will contain the TableView objects
    private final ScrollPane[] containers;
    // HBox that will be used to arrange the ScrollPanes
    private final HBox centerBox;
    // List to store SalesOrderData objects fetched from the database
    private List<SalesOrderData> dataList;
    // Set to store unique dayOfWeeks fetched from the database
    private Set<String> dayOfWeeks;

    /**
     * Constructor takes an HBox and a variable number of ScrollPane objects.
     *
     * @param centerBox  The HBox for arranging the ScrollPanes.
     * @param containers The variable number of ScrollPane objects.
     */
    public ExpandableReaderThread(HBox centerBox, ScrollPane... containers) {
        this.centerBox = centerBox;
        this.containers = containers;
    }

    /**
     * Method to clear existing tables from the centerBox and ScrollPanes
     */
    private void clearExistingTables() {
        // Log a message indicating that the tables are being updated
        LOGGER.info("Updating tables");

        // Run the following code on the JavaFX Application Thread
        Platform.runLater(() -> {
            // Clear all the children of the centerBox (which holds the tables)
            this.centerBox.getChildren().clear();

            // Iterate through each ScrollPane in the containers array
            for (ScrollPane scrollPane : this.containers) {
                // Set the content of the ScrollPane to null, effectively removing any content it currently holds
                scrollPane.setContent(null);
            }
        });
    }

    /**
     * Fetches SalesOrderData and dayOfWeeks from the database.
     */
    private void fetchData() {
        // Initialize dataList as an empty ArrayList
        this.dataList = new ArrayList<>();
        // Initialize dayOfWeeks as a TreeSet, which will store unique elements in sorted order
        this.dayOfWeeks = new TreeSet<>();

        // Iterate through each company in the Query.Company enumeration
        for(Query.Company company : Query.Company.values()) {
            // Log a message indicating that data is being queried for the current company
            LOGGER.info("Querying data for {}", company);
            // Create a new Query object with the current company as a parameter
            Query query = new Query(company);
            // Add all the SalesOrderData returned by queryDB() to the dataList
            this.dataList.addAll(query.queryDB());
            // Add all the day of weeks returned by getDayOfWeeks() to the dayOfWeeks Set
            this.dayOfWeeks.addAll(query.getDayOfWeeks());
        }
    }


    /**
     * Creates a set of SalesOrder objects from the fetched SalesOrderData objects.
     *
     * @return A set of SalesOrder objects created from the fetched data.
     */
    private Set<SalesOrder> createSalesOrders() {

        // Initialize salesOrderSet as an empty Set to store SalesOrder objects
        Set<SalesOrder> salesOrderSet = new HashSet<>();

        // Iterate through each SalesOrderData object in the dataList
        for (SalesOrderData salesOrderData : this.dataList) {
            // Create placeholder variable for SalesOrder
            SalesOrder targettedSalesOrder = null;
            // Iterate through each SaleOrder object in the salesOrderSet
            for(SalesOrder salesOrder : salesOrderSet) {
                // If a SalesOrder sale order number (so ID) and due date matches, initialize the placeholder
                if(salesOrder.getSo().equals(salesOrderData.getSalesOrderID()) && salesOrder.getDueDate().equals(salesOrderData.getDueDate())) {
                    targettedSalesOrder = salesOrder;
                    break;
                }
            }

            if(targettedSalesOrder == null) {
                // If no target was found, create new SaleOrder object
                targettedSalesOrder = new SalesOrder(salesOrderData.getSalesOrderID(), salesOrderData.getDueDate());
                // Set the new SaleOrder object with first part in list
                targettedSalesOrder.setPartsList(new ArrayList<>(Collections.singleton(new SalesOrder.Parts(salesOrderData.getPid(), salesOrderData.getQuantities()))));
            } else {
                // If a target was found, get the target's parts list
                List<SalesOrder.Parts> parts = targettedSalesOrder.getPartsList();
                // Add new part to target's parts list
                parts.add(new SalesOrder.Parts(salesOrderData.getPid(), salesOrderData.getQuantities()));
            }

            // Add saleorder to SalesOrderSet
            salesOrderSet.add(targettedSalesOrder);
        }

        // Return the list of SalesOrder objects
        return salesOrderSet;
    }

    /**
     * Waits for GUI elements to finish rendering.
     */
    private void waitForGUIElements() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates the tables with the SalesOrder objects.
     *
     * @param salesOrderSet A set of SalesOrder objects to populate the tables.
     * @param sqlFormat      The DateFormat used for parsing SQL date strings.
     * @param dayOfWeekdf    The DateFormat used for formatting day of week strings.
     * @param tables         A map that associates day of the week strings with their respective TableView objects.
     */
    private void populateTable(Set<SalesOrder> salesOrderSet, DateFormat sqlFormat, DateFormat dayOfWeekdf, Map<String, TableView<SalesOrder>> tables) {
        // Iterate through each SalesOrder in the salesOrderSet
        for(SalesOrder salesOrder : salesOrderSet) {
            try {
                // Parse the dueDate of the SalesOrder using the sqlFormat DateFormat
                Date sqlDate = sqlFormat.parse(salesOrder.getDueDate());
                // Convert the parsed sqlDate to a day of the week string using the dayOfWeekdf DateFormat
                String dayOfWeekDate = dayOfWeekdf.format(sqlDate);
                // Run the following code on the JavaFX Application Thread
                Platform.runLater(() -> {
                    // Get the TableView for the day of the week from the tables Map
                    TableView<SalesOrder> tableView = tables.get(dayOfWeekDate);
                    // Add the SalesOrder to the TableView's items
                    tableView.getItems().add(salesOrder);
                });

            } catch (ParseException e) {
                // Log an error if there is an issue parsing the sales order date
                LOGGER.error("Error unable to parse sales order date for {}, date: {} " + e, salesOrder.getSo(), salesOrder.getDueDate());
                // Log the stack trace of the exception
                LOGGER.trace("{}", e.getMessage());
            }
        }
    }

    /**
     * Creates and configures a TableView for the given day of the week using the provided date formats.
     *
     * @param dayOfWeek   The day of the week as a string.
     * @param sqlFormat   The DateFormat used for parsing SQL date strings.
     * @param dayOfWeekdf The DateFormat used for formatting day of week strings.
     * @return The configured TableView object.
     * @throws ParseException If the day of the week string cannot be parsed.
     */
    private TableView<SalesOrder> createAndConfigureTableView(String dayOfWeek, DateFormat sqlFormat, DateFormat dayOfWeekdf) throws ParseException {
        // Create a new TableView for SalesOrder objects
        TableView<SalesOrder> table = new TableView<>();
        // Set the column resize policy to maintain equal column widths as the table resizes
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Parse the day of the week string and format it for display
        Date sqlDate = sqlFormat.parse(dayOfWeek);
        String dayOfWeekDate = dayOfWeekdf.format(sqlDate);

        // Create a parent TableColumn with the formatted day of the week as its title
        TableColumn dateCol = new TableColumn(dayOfWeekDate);

        // Create and configure the SO (Sales Order) column
        TableColumn soCol = new TableColumnBuilder("SO")
                .setSize(table, .3)
                .setCellFactory(param -> new SaleOrderCell())
                .setCellValueFactory(new PropertyValueFactory("so"))
                .build();

        // Create and configure the PID (Part ID) column
        TableColumn pidCol = new TableColumnBuilder("PID")
                .setCellFactory(param -> new PIDCell())
                .build();

        // Create and configure the Qty (Quantity) column
        TableColumn quantityCol = new TableColumnBuilder("Qty")
                .setCellFactory(param -> new QuantityCell())
                .setSize(table, .2)
                .build();

        // Add the SO, PID, and Qty columns as children of the parent dateCol
        dateCol.getColumns().add(soCol);
        dateCol.getColumns().add(pidCol);
        dateCol.getColumns().add(quantityCol);

        // Add the parent dateCol to the TableView
        table.getColumns().add(dateCol);

        // Return the configured TableView
        return table;
    }

    /**
     * The run method of the Runnable interface, which will be executed when the thread is started.
     */
    @Override
    public void run() {
        // Clear existing tables from the UI
        clearExistingTables();
        // Fetch data for SalesOrders from the database
        fetchData();
        // Check if there is any data in the dataList
        if(this.dataList.size() > 0) {
            // Create SalesOrder objects from the fetched data
            Set<SalesOrder> salesOrderList = createSalesOrders();

            // Set up the required date formats for parsing and formatting dates
            DateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0");
            DateFormat dayOfWeekdf = new SimpleDateFormat("EEEE");

            // Create a map to store the tables for each day of the week
            Map<String, TableView<SalesOrder>> tables = new HashMap<>();

            // Initialize the counter for iterating through the ScrollPane containers
            int counter = 0;
            // Iterate through the dayOfWeeks set
            for (String dayOfWeek : this.dayOfWeeks) {
                int finalCounter = counter;
                // Run the following code on the JavaFX Application Thread
                Platform.runLater(() -> {
                    try {
                        // Create and configure the TableView for the specific day of the week
                        TableView<SalesOrder> table = createAndConfigureTableView(dayOfWeek, sqlFormat, dayOfWeekdf);

                        // Add the configured TableView to the corresponding ScrollPane container
                        this.containers[finalCounter].setContent(table);

                        // Add the ScrollPane to the centerBox
                        this.centerBox.getChildren().add(this.containers[finalCounter]);

                        // Store the TableView in the tables map with the day of the week as the key
                        tables.put(dayOfWeekdf.format(sqlFormat.parse(dayOfWeek)), table);

                        // Log the creation of the table for the specific day of the week
                        LOGGER.info("Creating {} table", dayOfWeekdf.format(sqlFormat.parse(dayOfWeek)));
                    } catch (ParseException e) {
                        // Log an error if there is an issue parsing the day of the week date
                        LOGGER.error("Error unable to parse day of the week date for {}" + e, dayOfWeek);
                        // Log the stack trace of the exception
                        LOGGER.trace("{}", e.getMessage());
                    }
                });
                // Increment the counter
                counter++;
            }

            // Wait for the GUI elements to be ready
            waitForGUIElements();

            // Populate the tables with the SalesOrder data
            populateTable(salesOrderList, sqlFormat, dayOfWeekdf, tables);
        }
    }
}
