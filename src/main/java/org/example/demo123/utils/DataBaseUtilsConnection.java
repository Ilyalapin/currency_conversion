package org.example.demo123.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBaseUtilsConnection {
    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(
                "jdbc:sqlite:C:/Users/Илюха/Desktop/demo123/src/main/resources/mydatabase.db"
        );
    }
//    private static final HikariDataSource HIKARI_DATA_SOURCE;
//
//    static {
//        HikariConfig hikariConfig = new HikariConfig();
//
//        hikariConfig.setJdbcUrl("jdbc:sqlite::resource:databaselapin.db");
//        hikariConfig.setDriverClassName("org.sqlite.JDBC");
//        HIKARI_DATA_SOURCE = new HikariDataSource(hikariConfig);
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return HIKARI_DATA_SOURCE.getConnection();
//    }

}
