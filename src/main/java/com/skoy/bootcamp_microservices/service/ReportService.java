package com.skoy.bootcamp_microservices.service;

import com.skoy.bootcamp_microservices.dto.TransactionDTO;
import com.skoy.bootcamp_microservices.mapper.TransactionMapper;
import com.skoy.bootcamp_microservices.repository.IReportRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {

    private final IReportRepository repository;
    private final WebClient.Builder webClientBuilder;
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @Value("${account.service.url}")
    private String accountServiceUrl;

    @Value("${credit.service.url}")
    private String creditServiceUrl;

    @Value("${transaction.service.url}")
    private String transactionServiceUrl;


    @Override
    public Flux<TransactionDTO> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId)
                .map(TransactionMapper::toDto);
    }


    @Override
    public Mono<Map<String, BigDecimal>> getDailyAverageBalances(String customerId) {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().plus(1, ChronoUnit.MONTHS).withDayOfMonth(1).minusDays(1);

        return webClientBuilder.build()
                .get()
                .uri(transactionServiceUrl + "/transactions/customer/" + customerId)
                .retrieve()
                .bodyToFlux(TransactionDTO.class)
                .filter(transaction -> !transaction.getCreatedAt().toLocalDate().isBefore(startDate) &&
                        !transaction.getCreatedAt().toLocalDate().isAfter(endDate))
                .groupBy(TransactionDTO::getProductTypeId)
                .flatMap(groupedFlux -> groupedFlux.collectList()
                        .map(transactions -> {
                            BigDecimal totalBalance = transactions.stream()
                                    .map(TransactionDTO::getAmount)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            long daysInMonth = ChronoUnit.DAYS.between(startDate, endDate) + 1;
                            BigDecimal averageBalance = totalBalance.divide(BigDecimal.valueOf(daysInMonth), BigDecimal.ROUND_HALF_UP);
                            return Map.entry(groupedFlux.key(), averageBalance);
                        }))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    @Override
    public Mono<Map<String, BigDecimal>> getCommissionsByProduct(LocalDate startDate, LocalDate endDate) {
        return webClientBuilder.build()
                .get()
                .uri(transactionServiceUrl + "/transactions")
                .retrieve()
                .bodyToFlux(TransactionDTO.class)
                .filter(transaction -> !transaction.getCreatedAt().toLocalDate().isBefore(startDate) &&
                        !transaction.getCreatedAt().toLocalDate().isAfter(endDate))
                .filter(transaction -> transaction.getCommissionAmount() != null && transaction.getCommissionAmount().compareTo(BigDecimal.ZERO) > 0)
                .groupBy(TransactionDTO::getProductTypeId)
                .flatMap(groupedFlux -> groupedFlux.collectList()
                        .map(transactions -> {
                            BigDecimal totalCommission = transactions.stream()
                                    .map(TransactionDTO::getCommissionAmount)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            return Map.entry(groupedFlux.key(), totalCommission);
                        }))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }

}