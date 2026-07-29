package com.ymb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central place to open a JDBC connection to the Oracle database.
 *
 * Update DB_URL, DB_USER and DB_PASSWORD below to match your local
 * Oracle setup (same style used in the internship project).
 *
 * Requires ojdbc8.jar (or a matching Oracle JDBC driver) on the
 * project's build path / WEB-INF/lib.
 */
public class DBConnection {

    private static final String DB_URL =
            "jdbc:oracle:thin:@localhost:1521:xe"; // change 'xe' to your SID/service name

    private static final String DB_USER = "system";      // change to your DB username
    private static final String DB_PASSWORD = "password"; // change to your DB password

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found on classpath: " + e.getMessage());
        }
    }

    private DBConnection() {
        // utility class - no instances
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
