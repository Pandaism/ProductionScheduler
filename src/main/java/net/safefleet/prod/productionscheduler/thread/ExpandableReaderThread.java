package net.safefleet.prod.productionscheduler.thread;

import java.sql.*;

public class ExpandableReaderThread implements Runnable {
    @Override
    public void run() {
        String url = "jdbc:expandableerp://localhost:3306/mydatabase";
        String username = "user";
        String password = "password";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM employees");
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
