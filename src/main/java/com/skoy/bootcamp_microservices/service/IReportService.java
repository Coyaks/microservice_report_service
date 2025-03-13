package com.skoy.bootcamp_microservices.service;

import com.skoy.bootcamp_microservices.dto.TransactionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IReportService {

    Flux<TransactionDTO> findByCustomerId(String customerId);
    Mono<Map<String, BigDecimal>> getDailyAverageBalances(String customerId);
    Mono<Map<String, BigDecimal>> getCommissionsByProduct(LocalDate startDate, LocalDate endDate);
    Mono<Map<String, Object>> getCustomerSummary(String customerId);
    Mono<Map<String, Object>> getGeneralReportByProduct(String customerId, LocalDate startDate, LocalDate endDate);
    Mono<Map<String, List<TransactionDTO>>> getLast10Transactions(String customerId);
}
