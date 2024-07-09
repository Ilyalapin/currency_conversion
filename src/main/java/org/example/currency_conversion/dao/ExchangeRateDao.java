package org.example.currency_conversion.dao;

import org.example.currency_conversion.entity.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateDao extends Dao<ExchangeRate> {

    Optional<ExchangeRate> findByCodes(String baseCurrencyCode, String targetCurrencyCode);
}
