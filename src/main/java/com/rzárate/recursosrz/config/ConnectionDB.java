package main.java.com.rzárate.recursosrz.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static Connection connection;

    private ConnectionDB() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(CredentialsDB.URL, CredentialsDB.USER, CredentialsDB.PASS);
        }
        return connection;
    }
}
