package org.example.currency_conversion.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_conversion.dto.ExchangeRequestDto;
import org.example.currency_conversion.dto.ExchangeResponseDto;
import org.example.currency_conversion.service.ExchangeService;
import org.example.currency_conversion.utils.ValidationUtils;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange/*")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeService exchangeService = new ExchangeService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            IOException {

        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        ValidationUtils.validateCurrencyCode(baseCurrencyCode);
        ValidationUtils.validateCurrencyCode(targetCurrencyCode);
        ValidationUtils.validateAmount(amount);

        BigDecimal amountValue = new BigDecimal(amount);

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(
                baseCurrencyCode,
                targetCurrencyCode,
                amountValue
        );

        ExchangeResponseDto exchangeResponseDto = exchangeService.makeConverted(exchangeRequestDto);
        objectMapper.writeValue(resp.getOutputStream(), exchangeResponseDto);
    }
}
