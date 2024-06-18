package org.example.demo123.dao;

import org.example.demo123.Currency;

import java.util.Optional;

public interface CurrencyDao extends Dao<Currency, Long> {
     Optional<Currency> findByCode(String code);
}
