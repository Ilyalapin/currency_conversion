package org.example.currency_conversion.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_conversion.dao.ExchangeRateDaoImpl;
import org.example.currency_conversion.dto.ExchangeRateRequestDto;
import org.example.currency_conversion.entity.ExchangeRate;
import org.example.currency_conversion.exception.InvalidParameterException;
import org.example.currency_conversion.exception.NotFoundException;
import org.example.currency_conversion.service.ExchangeService;
import org.example.currency_conversion.utils.ValidationUtils;

import java.io.IOException;
import java.math.BigDecimal;


import static org.example.currency_conversion.utils.MappingUtils.convertToDto;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRateDaoImpl exchangeRateDao = new ExchangeRateDaoImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }


    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ExchangeService exchangeService = new ExchangeService();

        String currencyCode = req.getPathInfo().replaceFirst("/", "");
        String baseCurrencyCode = currencyCode.substring(0, 3);
        String targetCurrencyCode = currencyCode.substring(3);
        String requestBody = req.getReader().readLine();

        ValidationUtils.validateCurrencyCode(baseCurrencyCode);
        ValidationUtils.validateCurrencyCode(targetCurrencyCode);
        ValidationUtils.validateParameter(requestBody);

        String rate = requestBody.replace("rate=", "");
        BigDecimal rateValue;
        try {
            rateValue = new BigDecimal(rate);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Invalid parameter '" + rate + "' ,rate must be greater than zero");
        }

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(
                baseCurrencyCode,
                targetCurrencyCode,
                rateValue
        );

        ValidationUtils.validateExchange(exchangeRateRequestDto);

        ExchangeRate exchangeRate = exchangeService.update(exchangeRateRequestDto);
        objectMapper.writeValue(resp.getWriter(), convertToDto(exchangeRate));
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            IOException {
        String currencyCode = req.getPathInfo().replaceFirst("/", "");
        String baseCurrencyCode = currencyCode.substring(0, 3);
        String targetCurrencyCode = currencyCode.substring(3);

        ValidationUtils.validateCurrencyCode(baseCurrencyCode);
        ValidationUtils.validateCurrencyCode(targetCurrencyCode);

        ExchangeRate exchangeRate = exchangeRateDao.findByCodes(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new NotFoundException(
                        "Exchange rate with codes: '" + baseCurrencyCode + "', '" + targetCurrencyCode + "' not found")
                );

        objectMapper.writeValue(resp.getWriter(), convertToDto(exchangeRate));
    }
}

