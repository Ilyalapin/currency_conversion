package org.example.demo123.servlet;

import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.demo123.Currency;
import org.example.demo123.utils.ValidationUtils;
import org.example.demo123.dao.CurrencyDaoImpl;
import org.example.demo123.exception.NotFoundException;
import static org.example.demo123.utils.MappingUtils.convertToDto;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyDaoImpl currencyDaoImpl = new CurrencyDaoImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            IOException {
        String code = req.getPathInfo().replaceFirst("/", "");
        ValidationUtils.validateCurrencyCode(code);
         Currency currency = currencyDaoImpl.findByCode(code)
                 .orElseThrow(() -> new NotFoundException("Currency with code "+code+" not found"));
         objectMapper.writeValue(resp.getWriter(), convertToDto(currency));
        }
    }


