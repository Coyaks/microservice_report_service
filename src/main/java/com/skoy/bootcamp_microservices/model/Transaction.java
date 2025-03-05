package com.skoy.bootcamp_microservices.model;

import com.skoy.bootcamp_microservices.enums.ProductTypeEnum;
import com.skoy.bootcamp_microservices.enums.TransactionStatusEnum;
import com.skoy.bootcamp_microservices.enums.TransactionTypeEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//@Data
//@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String customerId;
    private ProductTypeEnum productType;
    private String productTypeId;
    private TransactionTypeEnum transactionType; // DEPOSIT, WITHDRAWAL, TRANSFER
    private BigDecimal amount;
    private TransactionStatusEnum status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}