package org.example.currency_conversion.utils;

import org.example.currency_conversion.dto.CurrencyRequestDto;

import org.example.currency_conversion.dto.ExchangeRateRequestDto;
import org.example.currency_conversion.exception.InvalidParameterException;

import java.math.BigDecimal;


public class ValidationUtils {


    public static void validateCurrencyCode(String code) {
        if (code.length() != 3) {
            throw new InvalidParameterException("Invalid parameter '" + code + "' ,Currency code must be 3 letters");
        }
        if (!(code.matches("[A-Z]{3}"))) {
            throw new InvalidParameterException("Invalid parameter '" + code + "' ,Currency code must be 3 uppercase letters");
        }
    }


    public static void validateCurrency(CurrencyRequestDto currencyRequestDto) {
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


    public static void validateExchange(ExchangeRateRequestDto exchangeRateRequestDto) {
        String baseCurrencyCode = exchangeRateRequestDto.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.getTargetCurrencyCode();
        BigDecimal rate = exchangeRateRequestDto.getRate();

        if (baseCurrencyCode == null || baseCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - baseCurrencyCode");
        }
        if (targetCurrencyCode == null || targetCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - targetCurrencyCode");
        }
        if (rate == null) {
            throw new InvalidParameterException("Missing parameter - rate");
        }
        if (rate.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidParameterException("Invalid parameter '" + rate + "' Rate must be greater than zero");
        }

        validateCurrencyCode(baseCurrencyCode);
        validateCurrencyCode(targetCurrencyCode);
    }


    public static void validateParameter(String parameter) {

        if (parameter == null || parameter.isBlank()) {
            throw new InvalidParameterException("Missing parameter - " + parameter);
        }
    }


    public static void validateAmount(String amount) {

        if (amount.matches("[A-z]{3}")) {
            throw new InvalidParameterException("Invalid parameter '" + amount + "' ,'amount'  must be number greater than zero");
        }
        if (amount.isBlank()) {
            throw new InvalidParameterException("Missing parameter - amount");
        }
    }
}
