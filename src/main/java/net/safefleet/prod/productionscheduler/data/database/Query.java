package net.safefleet.prod.productionscheduler.data.database;

import net.safefleet.prod.productionscheduler.ProductionScheduler;
import net.safefleet.prod.productionscheduler.data.SalesOrder;
import net.safefleet.prod.productionscheduler.data.files.ShippableItemFile;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Query {
    public enum CORP {
        COBAN("coban"), FLEETMIND("fleetmind"), SEON("seon");

        private final String corp;
        CORP(String corp) {
            this.corp = corp;
        }

        public String getCorp() {
            return corp;
        }
    }

    private final String corp;
    private String database;
    private final Map<String, List<SalesOrder.Parts>> orderMap = new HashMap<>();

    public Query(CORP corp) {
        this.corp = corp.getCorp();
    }

    private Connection getConnection() throws SQLException {
        String username = ProductionScheduler.properties.getProperty(this.corp + ".db.user");
        String password = ProductionScheduler.properties.getProperty(this.corp + ".db.password");
        this.database = ProductionScheduler.properties.getProperty(this.corp + ".db.database");
        String ip = ProductionScheduler.properties.getProperty(this.corp + ".db.url");
        String wsid = ProductionScheduler.properties.getProperty(this.corp + ".db.wsid");
        String url = "jdbc:sqlserver://" + ip + ":1433;databaseName=" + this.database + ";user="+username+";password="+password+";wsid="+wsid;

        return DriverManager.getConnection(url);
    }

    private ResultSet getResultSet(Connection connection) throws SQLException {
        LocalDate now = LocalDate.now();
        LocalDate monday = now.with(DayOfWeek.MONDAY);
        LocalDate friday = now.with(DayOfWeek.FRIDAY);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");

        String soMasterQuery = "SELECT SOFOD.SO_ID, SOFOD.Part_ID, SOFOD.REV_SHIP_DATE, SOFOD.ORDER_QTY, SOFOM.SO_TYPE \n" +
                "FROM " + this.database + ".dbo.SOFOD SOFOD, " + this.database + ".dbo.SOFOM SOFOM\n" +
                "WHERE (SOFOD.SO_ID = SOFOM.SO_ID) \n" +
                "AND (" + parseShippableItems(new ShippableItemFile(ShippableItemFile.SOURCE.valueOf(this.corp.toUpperCase())).read()) + ")\n" +
                "AND (REV_SHIP_DATE BETWEEN '" + df.format(monday) + "' and '" + df.format(friday) + "')\n" +
                "AND (SOFOM.SO_TYPE = 'NS' AND SOFOM.ORDER_CLASS != 'R' AND SOFOM.ORDER_CLASS != 'R1'  AND SOFOM.ORDER_CLASS != 'R2' AND SOFOM.ORDER_CLASS != 'R3')" + "\n" +
                "AND SOFOD.SO_LINE_STATUS = 'O'";
        Statement statement = connection.createStatement();

        return statement.executeQuery(soMasterQuery);
    }

    public Map<String, List<SalesOrder.Parts>> queryDB() {
        Connection connection = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            if (connection != null) {
                System.out.println(this.corp + ": Connected to Expandable");
                rs = getResultSet(connection);

                while (rs.next()) {
                    String so = rs.getString("SO_ID");
                    String part = rs.getString("Part_ID");
                    String shipDate = rs.getString("REV_SHIP_DATE");
                    String quantity = rs.getString("ORDER_QTY");

                    if(!this.orderMap.containsKey(so)) {
                        this.orderMap.put(so, new ArrayList<>());
                    }

                    List<SalesOrder.Parts> partsList = this.orderMap.get(so);
                    partsList.add(new SalesOrder.Parts(part, Integer.parseInt(quantity.substring(0, quantity.indexOf('.'))), shipDate));

                    this.orderMap.put(so, partsList);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) {
                    rs.close();
                    System.out.println(this.corp + ": ResultSet closed");
                }

                if(connection != null) {
                    connection.close();
                    System.out.println(this.corp + ": Connection to Expandable closed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return this.orderMap;
    }


    private static String parseShippableItems(List<String> shippableItems) {
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
}
