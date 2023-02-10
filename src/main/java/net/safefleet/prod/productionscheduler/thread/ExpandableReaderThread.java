package net.safefleet.prod.productionscheduler.thread;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.safefleet.prod.productionscheduler.ProductionScheduler;
import net.safefleet.prod.productionscheduler.data.SalesOrder;
import net.safefleet.prod.productionscheduler.data.files.ShippableItemFile;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class ExpandableReaderThread implements Runnable {
    private TableContainer[] containers;

    public ExpandableReaderThread(TableContainer... containers) {
        this.containers = containers;
    }

    @Override
    public void run() {
        System.out.println("Updating tables");
        for(TableContainer tableContainer : this.containers) {
            tableContainer.getTableView().getItems().clear();
        }

        String username = ProductionScheduler.properties.getProperty("db.user");
        String password = ProductionScheduler.properties.getProperty("db.password");
        String database = ProductionScheduler.properties.getProperty("db.database");
        String ip = ProductionScheduler.properties.getProperty("db.url");
        String wsid = ProductionScheduler.properties.getProperty("db.wsid");
        String url = "jdbc:sqlserver://" + ip + ":1433;databaseName=" + database + ";user="+username+";password="+password+";wsid="+wsid;


        LocalDate now = LocalDate.now();
        LocalDate monday = now.with(DayOfWeek.MONDAY);
        LocalDate friday = now.with(DayOfWeek.FRIDAY);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");

        String soMasterQuery = "SELECT SOFOD.SO_ID, SOFOD.Part_ID, SOFOD.REV_SHIP_DATE, SOFOD.ORDER_QTY, SOFOM.SO_TYPE \n" +
                "FROM COBANDB.dbo.SOFOD SOFOD, COBANDB.dbo.SOFOM SOFOM\n" +
                "WHERE (SOFOD.SO_ID = SOFOM.SO_ID) \n" +
                "AND (" + parseShippableItems(new ShippableItemFile().read()) + ")\n" +
                "AND (REV_SHIP_DATE BETWEEN '" + df.format(monday) + "' and '" + df.format(friday) + "')\n" +
                "AND (SOFOM.SO_TYPE = 'NS' AND SOFOM.ORDER_CLASS != 'R' AND SOFOM.ORDER_CLASS != 'R1'  AND SOFOM.ORDER_CLASS != 'R2' AND SOFOM.ORDER_CLASS != 'R3')" + "\n" +
                "AND SOFOD.SO_LINE_STATUS = 'O'";
        Connection connection = null;
        ResultSet rs = null;

        Map<String, List<SalesOrder.Parts>> orderMap = new HashMap<>();
        try {
            connection = DriverManager.getConnection(url);
            if (connection != null) {
                System.out.println("Connected to Expandable");

                Statement statement = connection.createStatement();
                rs = statement.executeQuery(soMasterQuery);

                while (rs.next()) {
                    String so = rs.getString("SO_ID");
                    String part = rs.getString("Part_ID");
                    String shipDate = rs.getString("REV_SHIP_DATE");
                    String quantity = rs.getString("ORDER_QTY");

                    if(!orderMap.containsKey(so)) {
                        orderMap.put(so, new ArrayList<>());
                    }

                    List<SalesOrder.Parts> partsList = orderMap.get(so);
                    partsList.add(new SalesOrder.Parts(part, Integer.parseInt(quantity.substring(0, quantity.indexOf('.'))), shipDate));

                    orderMap.put(so, partsList);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) {
                    rs.close();
                    System.out.println("ResultSet closed");
                }

                if(connection != null) {
                    connection.close();
                    System.out.println("Connection to Expandable closed");

                    for(String key : orderMap.keySet()) {
                        SalesOrder salesOrder = new SalesOrder(key);
                        salesOrder.setParts(orderMap.get(key));

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
                }
            } catch (SQLException | ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private String parseShippableItems(List<String> shippableItems) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < shippableItems.size(); i++) {
            if(i == shippableItems.size() - 1) {
                sb.append("SOFOD.Part_ID = '").append(shippableItems.get(i)).append("'");
            } else {
                sb.append("SOFOD.Part_ID = '").append(shippableItems.get(i)).append("' or ");
            }
        }

        return sb.toString();
    }

    public static class TableContainer {
        private TableView<SalesOrder> tableView;
        private TableColumn<SalesOrder, String> associatedColumn;

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
