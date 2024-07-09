package org.example.currency_conversion.dao;

import org.example.currency_conversion.entity.Currency;

import java.util.Optional;

public interface CurrencyDao extends Dao<Currency> {

    Optional<Currency> findByCode(String code);
}
