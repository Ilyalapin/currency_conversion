package org.example.currency_conversion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateRequestDto {

    private String baseCurrencyCode;

    private String targetCurrencyCode;

    private BigDecimal rate;
}
