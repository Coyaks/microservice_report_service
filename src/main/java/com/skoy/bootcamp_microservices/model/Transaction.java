package com.skoy.bootcamp_microservices.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String id;
    private String customerId;
    private ProductTypeEnum productType;
    private String productTypeId;
    private TransactionTypeEnum transactionType;
    private CardTypeEnum cardType;
    private String cardId;
    private BigDecimal amount;
    private TransactionStatusEnum status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal commissionAmount = BigDecimal.ZERO;

    public enum ProductTypeEnum {
        BANK_ACCOUNT("CUENTAS BANCARIAS"),
        CREDIT("CREDITOS");

        private final String name;

        ProductTypeEnum(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    public enum TransactionTypeEnum {
        DEPOSIT("Deposito"),
        WITHDRAWAL("Retiro"),
        TRANSFER("Transferencia");

        private final String name;

        TransactionTypeEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum TransactionStatusEnum {
        PENDING,
        APPROVED,
        REJECTED
    }

    public enum CardTypeEnum {
        DEBIT("Debito"),
        CREDIT("Credito");

        private final String name;

        CardTypeEnum(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    public static Mono<Void> createTransaction(WebClient.Builder webClientBuilder, Transaction transactionDataSend) {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:9004/api/v1/transactions")
                .bodyValue(transactionDataSend)
                .retrieve()
                .bodyToMono(Void.class);
    }


}