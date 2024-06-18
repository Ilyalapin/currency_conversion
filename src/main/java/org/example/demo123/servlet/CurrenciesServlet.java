package org.example.demo123.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo123.Currency;
import org.example.demo123.utils.MappingUtils;
import org.example.demo123.utils.ValidationUtils;
import org.example.demo123.dao.CurrencyDaoImpl;
import org.example.demo123.dto.CurrencyRequestDto;
import org.example.demo123.dto.CurrencyResponseDto;
import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyDaoImpl currencyDaoImpl = new CurrencyDaoImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
     IOException {
        List<Currency> currencies =currencyDaoImpl.findAll();
        List<CurrencyResponseDto> currensiesDto = currencies.stream()
                .map(MappingUtils::convertToDto)
                .collect(Collectors.toList());
        objectMapper.writeValue(resp.getWriter(), currensiesDto);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
      String name = req.getParameter("name");
      String code = req.getParameter("code");
      String sign = req.getParameter("sign");

      CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(name, code, sign);

        ValidationUtils.validate(currencyRequestDto);

        Currency currency = currencyDaoImpl.addCurrency(MappingUtils.convertToModel(currencyRequestDto));
        resp.setStatus(SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), MappingUtils.convertToDto(currency));
    }
}
