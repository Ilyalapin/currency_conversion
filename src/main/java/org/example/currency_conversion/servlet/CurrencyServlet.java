package org.example.currency_conversion.servlet;

import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.currency_conversion.entity.Currency;
import org.example.currency_conversion.utils.ValidationUtils;
import org.example.currency_conversion.dao.CurrencyDaoImpl;
import org.example.currency_conversion.exception.NotFoundException;

import static org.example.currency_conversion.utils.MappingUtils.convertToDto;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyDaoImpl currencyDaoImpl = new CurrencyDaoImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            IOException {
        String code = req.getPathInfo().substring(1);

        ValidationUtils.validateCurrencyCode(code);

        Currency currency = currencyDaoImpl.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + code + "' not found"));

        objectMapper.writeValue(resp.getWriter(), convertToDto(currency));
    }
}


