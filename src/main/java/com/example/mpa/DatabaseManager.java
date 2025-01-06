package com.example.mpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    public static Connection getConnection() throws SQLException {
        try {

            return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error: Unable to establish a connection to the database.");
            e.printStackTrace();
            throw e;
        }
    }
}
