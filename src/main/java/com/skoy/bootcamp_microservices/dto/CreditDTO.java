package com.skoy.bootcamp_microservices.dto;

import com.skoy.bootcamp_microservices.enums.CreditTypeEnum;
import com.skoy.bootcamp_microservices.enums.CurrencyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDTO {
    private String id;
    private String customerId;
    private CreditTypeEnum creditType;
    private BigDecimal creditLimit;
    private BigDecimal availableCredit;
    private CurrencyEnum currency;

}
