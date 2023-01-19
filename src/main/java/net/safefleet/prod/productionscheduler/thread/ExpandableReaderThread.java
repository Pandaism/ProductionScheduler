package net.safefleet.prod.productionscheduler.thread;

import net.safefleet.prod.productionscheduler.ProductionScheduler;

import java.sql.*;

public class ExpandableReaderThread implements Runnable {
    @Override
    public void run() {
        String url = "jdbc:expandableerp://"+ ProductionScheduler.properties.getProperty("db.url") + ":3306/" + ProductionScheduler.properties.getProperty("db.database");
        String username = ProductionScheduler.properties.getProperty("db.user");
        String password = ProductionScheduler.properties.getProperty("db.password");

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT 1");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println(id + " " + name);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
