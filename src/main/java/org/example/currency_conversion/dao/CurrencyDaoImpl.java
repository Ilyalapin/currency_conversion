package org.example.currency_conversion.dao;

import org.example.currency_conversion.entity.Currency;
import org.example.currency_conversion.exception.DataBaseException;
import org.example.currency_conversion.exception.EntitiesException;
import org.example.currency_conversion.utils.DataBaseUtilsConnection;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDaoImpl implements CurrencyDao {

    @Override
    public Currency add(Currency entity) {
        final String query = "INSERT INTO Currencies (code, full_name, sign) VALUES (?,?,?) RETURNING *";

        try (Connection connection = DataBaseUtilsConnection.getConnection();

             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign());

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                throw new DataBaseException("Failed to add currency with code: '" + entity.getCode() + "' to the database");
            }
            return getCurrency(resultSet);

        } catch (SQLException exception) {
            if (exception instanceof SQLiteException) {
                SQLiteException sqLiteException = (SQLiteException) exception;
                if (sqLiteException.getResultCode().code == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                    throw new EntitiesException("Currency with code: '" + entity.getCode() + "' already exists");
                }
            }
            throw new DataBaseException("Failed to add currency with code: '" + entity.getCode() + "' to the database");
        }
    }

    @Override
    public List<Currency> findAll() {
        final String query = "SELECT * FROM Currencies";

        try (Connection connection = DataBaseUtilsConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(getCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException exception) {
            throw new DataBaseException("Failed to find currencies to the database");
        }
    }

    public Currency getCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign"));
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        final String query = "SELECT * FROM Currencies WHERE code = ?";

        try (Connection connection = DataBaseUtilsConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, code);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getCurrency(resultSet));
            }
        } catch (SQLException exception) {
            throw new DataBaseException("Failed to find currency with code: '" + code + "' to the database");
        }
        return Optional.empty();
    }
}
