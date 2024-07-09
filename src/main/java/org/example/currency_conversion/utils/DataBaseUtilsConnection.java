package org.example.currency_conversion.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;


public class DataBaseUtilsConnection {

    private static final HikariDataSource HIKARI_DATA_SOURCE;

    static {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:sqlite::resource:mydatabase.db");
        hikariConfig.setDriverClassName("org.sqlite.JDBC");

        HIKARI_DATA_SOURCE = new HikariDataSource(hikariConfig);
    }


    public static Connection getConnection() throws SQLException {
        return HIKARI_DATA_SOURCE.getConnection();
    }
}
