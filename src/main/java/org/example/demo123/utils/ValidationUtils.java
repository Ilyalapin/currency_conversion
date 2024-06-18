package org.example.demo123.utils;

import org.example.demo123.dto.CurrencyRequestDto;

import java.security.InvalidParameterException;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {
    private static Set<String> currencyCodes;

    public static void validateCurrencyCode(String code) {
        if (code.length() != 3) {
            throw new InvalidParameterException("Currency code must be 3 letters");
        }
        if(currencyCodes == null) {
            Set<Currency> currencies = Currency.getAvailableCurrencies();
            currencyCodes = currencies.stream()
                    .map(Currency::getCurrencyCode)
                    .collect(Collectors.toSet());
        }
        if (!currencyCodes.contains(code)) {
            throw new InvalidParameterException("Currency code must be 3 uppercase letters");
        }
    }
    public static void validate(CurrencyRequestDto currencyRequestDto) {
        String code = currencyRequestDto.getCode();
        String name = currencyRequestDto.getName();
        String sign = currencyRequestDto.getSign();
        if (code == null || code.isBlank()) {
            throw new InvalidParameterException("Missing parameter - code");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidParameterException("Missing parameter - name");
        }
        if (sign == null || sign.isBlank()) {
            throw new InvalidParameterException("Missing parameter - sign");
        }
        validateCurrencyCode(code);

    }
}
