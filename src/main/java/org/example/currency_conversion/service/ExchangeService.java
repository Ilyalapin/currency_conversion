package org.example.currency_conversion.service;

import org.example.currency_conversion.dao.CurrencyDaoImpl;
import org.example.currency_conversion.dao.ExchangeRateDaoImpl;
import org.example.currency_conversion.dto.ExchangeRateRequestDto;
import org.example.currency_conversion.dto.ExchangeRequestDto;
import org.example.currency_conversion.dto.ExchangeResponseDto;
import org.example.currency_conversion.entity.Currency;
import org.example.currency_conversion.entity.ExchangeRate;
import org.example.currency_conversion.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.RoundingMode.HALF_DOWN;
import static org.example.currency_conversion.utils.MappingUtils.convertToDto;

public class ExchangeService {

    ExchangeRateDaoImpl exchangeRateDao = new ExchangeRateDaoImpl();
    CurrencyDaoImpl currencyDao = new CurrencyDaoImpl();


    public ExchangeRate update(ExchangeRateRequestDto exchangeRateRequestDto) {

        String baseCurrencyCode = exchangeRateRequestDto.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.getTargetCurrencyCode();

        Currency baseCurrency = currencyDao.findByCode(baseCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Base Currency Not Found"));

        Currency targetCurrency = currencyDao.findByCode(targetCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Target Currency Not Found"));

        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, exchangeRateRequestDto.getRate());

        return exchangeRateDao.update(exchangeRate)
                .orElseThrow(() -> new NotFoundException(
                        "Failed to update exchange rate '" + baseCurrencyCode + "' - '" + targetCurrencyCode + "', no such exchange rate found")
                );
    }


    private Optional<ExchangeRate> findByDirectRate(ExchangeRequestDto exchangeRequestDto) {

        return exchangeRateDao.findByCodes(exchangeRequestDto.getBaseCurrencyCode(), exchangeRequestDto.getTargetCurrencyCode());
    }


    private Optional<ExchangeRate> findByReverseRate(ExchangeRequestDto exchangeRequestDto) {

        Optional<ExchangeRate> exchangeRateOptional = exchangeRateDao.
                findByCodes(
                        exchangeRequestDto.getTargetCurrencyCode(),
                        exchangeRequestDto.getBaseCurrencyCode()
                );
        if (exchangeRateOptional.isEmpty()) {
            return Optional.empty();
        }
        ExchangeRate exchangeRate = exchangeRateOptional.get();

        BigDecimal reverseRate = new BigDecimal(1).divide(exchangeRate.getRate(), 6, HALF_DOWN);

        ExchangeRate resultExchangeRate = new ExchangeRate(
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                reverseRate
        );
        return Optional.of(resultExchangeRate);
    }


    private Optional<ExchangeRate> findByCrossRate(ExchangeRequestDto exchangeRequestDto) {

        Optional<ExchangeRate> baseCodeUsdOptional = exchangeRateDao.
                findByCodes("USD", exchangeRequestDto.getBaseCurrencyCode());

        Optional<ExchangeRate> targetCodeUsdOptional = exchangeRateDao.
                findByCodes("USD", exchangeRequestDto.getTargetCurrencyCode());

        if (baseCodeUsdOptional.isEmpty() || targetCodeUsdOptional.isEmpty()) {
            return Optional.empty();
        }

        ExchangeRate baseCodeUsd = baseCodeUsdOptional.get();
        ExchangeRate targetCodeUsd = targetCodeUsdOptional.get();

        BigDecimal resultRate = targetCodeUsd.getRate()
                .divide(baseCodeUsd.getRate(), 6, HALF_DOWN);

        ExchangeRate resultExchangeRate = new ExchangeRate(
                baseCodeUsd.getTargetCurrency(),
                targetCodeUsd.getTargetCurrency(),
                resultRate
        );
        return Optional.of(resultExchangeRate);
    }


    private Optional<ExchangeRate> findExchangeRate(ExchangeRequestDto exchangeRequestDto) {

        Optional<ExchangeRate> exchangeRate = findByDirectRate(exchangeRequestDto);

        if (exchangeRate.isEmpty()) {
            exchangeRate = findByReverseRate(exchangeRequestDto);
        }
        if (exchangeRate.isEmpty()) {
            exchangeRate = findByCrossRate(exchangeRequestDto);
        }
        return exchangeRate;
    }


    public BigDecimal getConvertedAmount(BigDecimal rate, BigDecimal amount) {
        return rate.multiply(amount).setScale(2, HALF_DOWN);
    }


    public ExchangeResponseDto makeConverted(ExchangeRequestDto exchangeRequestDto) {

        ExchangeRate exchangeRate = findExchangeRate(exchangeRequestDto)
                .orElseThrow(() -> new NotFoundException("Exchange Rates Not Found"));

        BigDecimal amount = exchangeRequestDto.getAmount();
        BigDecimal rate = exchangeRate.getRate();
        BigDecimal convertedAmount = getConvertedAmount(rate, amount).setScale(2, HALF_DOWN);

        return new ExchangeResponseDto(
                convertToDto(exchangeRate.getBaseCurrency()),
                convertToDto(exchangeRate.getTargetCurrency()),
                rate,
                amount,
                convertedAmount
        );
    }
}



