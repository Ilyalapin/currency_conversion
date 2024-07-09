package org.example.currency_conversion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    private Long id;

    private String code;

    private String fullName;

    private String sign;

}
