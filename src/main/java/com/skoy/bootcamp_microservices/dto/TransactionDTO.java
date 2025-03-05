package com.skoy.bootcamp_microservices.dto;

import com.skoy.bootcamp_microservices.enums.ProductTypeEnum;
import com.skoy.bootcamp_microservices.enums.TransactionStatusEnum;
import com.skoy.bootcamp_microservices.enums.TransactionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String id;
    private String customerId;
    private ProductTypeEnum productType;
    private String productTypeId;
    private TransactionTypeEnum transactionType;
    private BigDecimal amount;
    private TransactionStatusEnum status;
    private LocalDateTime createdAt;
    private BigDecimal commissionAmount;

}
