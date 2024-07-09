package org.example.currency_conversion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateResponseDto {

    private Long id;

    private CurrencyResponseDto baseCurrency;

    private CurrencyResponseDto targetCurrency;

    private BigDecimal rate;
}
