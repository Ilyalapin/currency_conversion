package org.example.currency_conversion.dao;

import org.example.currency_conversion.dto.ExchangeRateRequestDto;
import org.example.currency_conversion.entity.Currency;
import org.example.currency_conversion.entity.ExchangeRate;
import org.example.currency_conversion.exception.DataBaseException;
import org.example.currency_conversion.exception.EntitiesException;
import org.example.currency_conversion.exception.NotFoundException;
import org.example.currency_conversion.utils.DataBaseUtilsConnection;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDaoImpl implements ExchangeRateDao {

    @Override
    public Optional<ExchangeRate> findByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        final String query = "SELECT er.id AS id," +
                "bc.id AS base_id," +
                " bc.code AS base_code," +
                " bc.full_name AS base_name," +
                " bc.sign AS base_sign," +
                " tc.id AS target_id," +
                " tc.code AS target_code," +
                " tc.full_name AS target_name," +
                " tc.sign AS target_sign," +
                " er.rate AS rate " +
                "FROM exchange_rates er " +
                "JOIN currencies bc ON er.base_currency_id = bc.id " +
                "JOIN currencies tc ON er.target_currency_id = tc.id" +
                " WHERE (base_currency_id =( SELECT c1.id FROM currencies c1 WHERE c1.code = ?) " +
                "AND target_currency_id = ( SELECT c2.id FROM currencies c2  WHERE c2.code = ?))";

        try (Connection connection = DataBaseUtilsConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(getExchangeRate(resultSet));
            }
        } catch (SQLException exception) {
            throw new DataBaseException("Failed to find exchange rates to the database");
        }
        return Optional.empty();
    }


    @Override
    public ExchangeRate add(ExchangeRate exchangeRate) {
        final String query = "INSERT INTO Exchange_rates (base_currency_id, target_currency_id, rate)" +
                " VALUES (?, ?, ?) RETURNING id";

        try (Connection connection = DataBaseUtilsConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, exchangeRate.getBaseCurrency().getId());
            statement.setLong(2, exchangeRate.getTargetCurrency().getId());
            statement.setBigDecimal(3, exchangeRate.getRate());

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new DataBaseException("Failed to add exchange rate to the database");
            }
            exchangeRate.setId(resultSet.getLong("id"));
            return exchangeRate;
        } catch (SQLException exception) {
            if (exception instanceof SQLiteException) {
                SQLiteException sqLiteException = (SQLiteException) exception;
                if (sqLiteException.getResultCode().code == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                    throw new EntitiesException("Exchange rate already exists");
                }
            }
            throw new DataBaseException("Failed to add exchange rate to the database");
        }
    }

    public ExchangeRate addByCode(ExchangeRateRequestDto exchangeRateRequestDto) {

        CurrencyDaoImpl currencyDao = new CurrencyDaoImpl();
        String baseCurrencyCode = exchangeRateRequestDto.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.getTargetCurrencyCode();

        Currency baseCurrency = currencyDao.findByCode(baseCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Currency with code: " + baseCurrencyCode + " not found"));

        Currency targetCurrency = currencyDao.findByCode(targetCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Currency with code: " + targetCurrencyCode + " not found"));

        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, exchangeRateRequestDto.getRate());

        return add(exchangeRate);
    }


    public Optional<ExchangeRate> update(ExchangeRate exchangeRate) {
        final String query = "UPDATE Exchange_Rates " +
                "SET rate = ? " +
                "WHERE  " +
                "base_currency_id = ? " +
                " AND " +
                "target_currency_id = ?" +
                " RETURNING id";

        try (Connection connection = DataBaseUtilsConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBigDecimal(1, exchangeRate.getRate());
            statement.setLong(2, exchangeRate.getBaseCurrency().getId());
            statement.setLong(3, exchangeRate.getTargetCurrency().getId());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                exchangeRate.setId(resultSet.getLong("id"));
                return Optional.of(exchangeRate);
            }
        } catch (SQLException exception) {
            throw new DataBaseException("Failed to update exchange rate to the database123");
        }
        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> findAll() {
        final String query = "SELECT er.id," +
                "bc.id  base_id," +
                " bc.code base_code," +
                " bc.full_name base_name," +
                " bc.sign base_sign," +
                " tc.id target_id," +
                " tc.code target_code," +
                " tc.full_name target_name," +
                " tc.sign target_sign," +
                " er.rate AS rate " +
                "FROM Exchange_Rates er " +
                "JOIN Currencies bc ON er.base_currency_id = bc.id " +
                "JOIN Currencies tc ON er.target_currency_id = tc.id";

        try (Connection connection = DataBaseUtilsConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            List<ExchangeRate> exchangeRates = new ArrayList<>();

            while (resultSet.next()) {
                exchangeRates.add(getExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException exception) {
            throw new DataBaseException("Failed to find exchange rates to the database");
        }
    }

    public ExchangeRate getExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getLong("id"),
                new Currency(
                        resultSet.getLong("base_id"),
                        resultSet.getString("base_code"),
                        resultSet.getString("base_name"),
                        resultSet.getString("base_sign")
                ),
                new Currency(
                        resultSet.getLong("target_id"),
                        resultSet.getString("target_code"),
                        resultSet.getString("target_name"),
                        resultSet.getString("target_sign")
                ),
                resultSet.getBigDecimal("rate"));
    }
}
