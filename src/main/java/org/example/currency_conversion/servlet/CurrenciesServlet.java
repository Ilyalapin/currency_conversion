package org.example.currency_conversion.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_conversion.entity.Currency;
import org.example.currency_conversion.utils.MappingUtils;
import org.example.currency_conversion.utils.ValidationUtils;
import org.example.currency_conversion.dao.CurrencyDaoImpl;
import org.example.currency_conversion.dto.CurrencyRequestDto;
import org.example.currency_conversion.dto.CurrencyResponseDto;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyDaoImpl currencyDaoImpl = new CurrencyDaoImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            IOException {
        List<Currency> currencies = currencyDaoImpl.findAll();
        List<CurrencyResponseDto> currensiesDto = new ArrayList<>();

        for (Currency dao : currencies) {
            currensiesDto.add(MappingUtils.convertToDto(dao));
        }
        objectMapper.writeValue(resp.getWriter(), currensiesDto);

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(name, code, sign);

        ValidationUtils.validateCurrency(currencyRequestDto);

        Currency currency = currencyDaoImpl.add(MappingUtils.convertToEntity(currencyRequestDto));
        resp.setStatus(SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), MappingUtils.convertToDto(currency));
    }
}
