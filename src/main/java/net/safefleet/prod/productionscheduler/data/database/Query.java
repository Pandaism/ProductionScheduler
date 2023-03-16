package net.safefleet.prod.productionscheduler.data.database;

import net.safefleet.prod.productionscheduler.ProductionScheduler;
import net.safefleet.prod.productionscheduler.data.SalesOrderData;
import net.safefleet.prod.productionscheduler.data.files.ShippableItemFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Query class is responsible for querying the database for sales order data
 * based on the specified company.
 */
public class Query {
    // Declare a logger for debugging and logging purposes
    private static final Logger LOGGER = LoggerFactory.getLogger(Query.class);
    // Enum representing different companies
    public enum Company {
        COBAN("coban"), FLEETMIND("fleetmind"), SEON("seon");

        private final String company;

        // Company constructor
        Company(String company) {
            this.company = company;
        }
        // Getter method for the corp string
        public String getCompany() {
            return company;
        }
    }

    // Declare variables for the class
    private final String corp;
    private final List<SalesOrderData> partsList = new ArrayList<>();
    private final List<String> dayOfWeeks = new ArrayList<>();
    private String database;

    /**
     * Query constructor initializes the corp variable.
     *
     * @param company The company to query data for.
     */
    public Query(Company company) {
        this.corp = company.getCompany();
    }

    /**
     * Query the database for sales order data.
     *
     * @return A list of SalesOrderData objects containing the queried data.
     */
    public List<SalesOrderData> queryDB() {
        // Try to establish a database connection
        try (Connection connection = getConnection()) {
            // If the connection is successful, perform the query
            if(connection != null) {
                // Log the established connection
                LOGGER.info("Open connection to " + this.corp + " established");
                // Prepare the SQL statement
                PreparedStatement ps = connection.prepareStatement("SELECT SOFOD.SO_ID, SOFOD.Part_ID, SOFOD.REV_SHIP_DATE, SOFOD.ORDER_QTY, SOFOM.SO_TYPE \n" +
                        "FROM " + this.database + ".dbo.SOFOD SOFOD, " + this.database + ".dbo.SOFOM SOFOM\n" +
                        "WHERE (SOFOD.SO_ID = SOFOM.SO_ID) \n" +
                        "AND (" + parseShippableItems(new ShippableItemFile(ShippableItemFile.SOURCE.valueOf(this.corp.toUpperCase())).read()) + ")\n" +
                        "AND (REV_SHIP_DATE BETWEEN ? and ?)\n" +
                        "AND (SOFOM.SO_TYPE = 'NS' AND SOFOM.ORDER_CLASS NOT IN ('R', 'R1', 'R2', 'R3'))" + "\n" +
                        "AND SOFOD.SO_LINE_STATUS = 'O'");
                // Set the date range for the query
                LocalDate now = LocalDate.now();
                LocalDate monday = now.with(DayOfWeek.MONDAY);
                LocalDate friday = now.with(DayOfWeek.FRIDAY);
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");
                // Set the PreparedStatement parameters
                ps.setString(1, df.format(monday));
                ps.setString(2, df.format(friday));
                // Execute the query and process the result set
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Extract data from the result set
                        String so = rs.getString("SO_ID");
                        String part = rs.getString("Part_ID");
                        String shipDate = rs.getString("REV_SHIP_DATE");
                        String quantity = rs.getString("ORDER_QTY");
                        // Add the extracted data to the partsList
                        this.partsList.add(new SalesOrderData(so, part, Integer.parseInt(quantity.substring(0, quantity.indexOf('.'))), shipDate));
                        // Add the shipDate to the dayOfWeeks list if not already present
                        if (!this.dayOfWeeks.contains(shipDate)) {
                            this.dayOfWeeks.add(shipDate);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            // Log any SQL errors
            LOGGER.error("Error querying database: {} " + e, this.corp);
            LOGGER.trace("{}", e.getMessage());
        }
        // Return the partsList
        return this.partsList;
    }

    /**
     * Getter method for the dayOfWeeks list.
     *
     * @return A list of String objects representing the days of the week.
     */
    public List<String> getDayOfWeeks() {
        return dayOfWeeks;
    }

    /**
     * Get a database connection.
     *
     * @return A Connection object representing the established database connection.
     * @throws SQLException If there's an error connecting to the database.
     */
    private Connection getConnection() throws SQLException {
        // Retrieve database connection properties from the ProductionScheduler class
        String username = ProductionScheduler.properties.getProperty(this.corp + ".db.user");
        String password = ProductionScheduler.properties.getProperty(this.corp + ".db.password");
        this.database = ProductionScheduler.properties.getProperty(this.corp + ".db.database");
        String ip = ProductionScheduler.properties.getProperty(this.corp + ".db.url");
        String wsid = ProductionScheduler.properties.getProperty(this.corp + ".db.wsid");
        String url = "jdbc:sqlserver://" + ip + ":1433;databaseName=" + this.database + ";user=" + username + ";password=" + password + ";wsid=" + wsid;
        // If the username is "Disabled", log a warning and return null
        if (username.equals("Disabled")) {
            LOGGER.warn(this.corp + " is disabled");
            return null;
        }
        // Connect to the database using the provided connection properties
        return DriverManager.getConnection(url);
    }

    /**
     * Convert the list of shippable items to an SQL query string.
     *
     * @param shippableItems A list of String objects representing the shippable items.
     * @return A String representing the constructed SQL query string.
     */
    private static String parseShippableItems(List<String> shippableItems) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shippableItems.size(); i++) {
            // If it's the last item in the list, add the condition without the 'or' keyword
            if (i == shippableItems.size() - 1) {
                sb.append("SOFOD.Part_ID = '").append(shippableItems.get(i)).append("'");
            } else {
                // Otherwise, add the condition with the 'or' keyword
                sb.append("SOFOD.Part_ID = '").append(shippableItems.get(i)).append("' or ");
            }
        }
        // Return the constructed SQL query string
        return sb.toString();
    }
}