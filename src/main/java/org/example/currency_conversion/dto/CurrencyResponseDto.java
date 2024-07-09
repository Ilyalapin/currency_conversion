package org.example.currency_conversion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponseDto {

    private Long id;

    private String name;

    private String code;

    private String sign;


}
