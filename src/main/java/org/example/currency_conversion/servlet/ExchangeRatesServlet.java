package org.example.currency_conversion.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_conversion.dao.ExchangeRateDaoImpl;
import org.example.currency_conversion.dto.ExchangeRateRequestDto;
import org.example.currency_conversion.dto.ExchangeRateResponseDto;
import org.example.currency_conversion.entity.ExchangeRate;
import org.example.currency_conversion.exception.InvalidParameterException;
import org.example.currency_conversion.utils.MappingUtils;
import org.example.currency_conversion.utils.ValidationUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static org.example.currency_conversion.utils.MappingUtils.convertToDto;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private final ExchangeRateDaoImpl eDao = new ExchangeRateDaoImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<ExchangeRate> exchangeRates = eDao.findAll();
        List<ExchangeRateResponseDto> exchangeRateDtos = new ArrayList<>();

        for (ExchangeRate dao : exchangeRates) {
            exchangeRateDtos.add(MappingUtils.convertToDto(dao));
        }
        objectMapper.writeValue(resp.getWriter(), exchangeRateDtos);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws
            IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        ValidationUtils.validateParameter(rate);

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

        ExchangeRate exchangeRate = eDao.addByCode(exchangeRateRequestDto);
        resp.setStatus(SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), convertToDto(exchangeRate));
    }
}
