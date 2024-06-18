package org.example.demo123.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponseDto {
    private Long id;
    private String code;
    private String name;
    private String sign;
}
