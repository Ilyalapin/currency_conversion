package org.example.currency_conversion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {

    private Long id;

    private Currency baseCurrency;

    private Currency targetCurrency;

    private BigDecimal rate;

    public ExchangeRate(Currency baseCurrencyId, Currency targetCurrencyId, BigDecimal rate) {
        this.baseCurrency = baseCurrencyId;
        this.targetCurrency = targetCurrencyId;
        this.rate = rate;
    }
}
