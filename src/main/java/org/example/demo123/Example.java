package org.example.demo123;

import org.example.demo123.dao.CurrencyDaoImpl;
import org.example.demo123.utils.DataBaseUtilsConnection;

import java.sql.*;
import java.util.List;
import java.util.Optional;



public class Example {
    public static void main(String[] args) {
//        CurrencyDaoImpl dao = new CurrencyDaoImpl();
//        List<Currency> findAll = dao.findAll();
//        System.out.println("currency "+dao.findAll());
//CurrencyDaoImpl dao = new CurrencyDaoImpl();
//Optional<Currency> byID = dao.findByCode("USD");
//byID.ifPresent(currency -> System.out.println("currency "+currency));


        final String query = "SELECT * FROM Currencies WHERE id = ?";

        try (Connection connection = DataBaseUtilsConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, 8);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Currency currency = getCurrency(resultSet);

                System.out.println("currency = " + currency);
            }
        }
        catch (SQLException e) {
            // TODO: handle exception
        }

    }
    private static Currency getCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }

}
