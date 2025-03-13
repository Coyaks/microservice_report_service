package com.skoy.bootcamp_microservices.service;

import com.skoy.bootcamp_microservices.dto.TransactionDTO;
import com.skoy.bootcamp_microservices.mapper.TransactionMapper;
import com.skoy.bootcamp_microservices.model.Transaction;
import com.skoy.bootcamp_microservices.repository.IReportRepository;
import com.skoy.bootcamp_microservices.utils.UDate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {

    private final IReportRepository repository;
    private final WebClient.Builder webClientBuilder;
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Value("${services.customer}")
    private String customerServiceUrl;

    @Value("${services.bankaccount}")
    private String accountServiceUrl;

    @Value("${services.credit}")
    private String creditServiceUrl;

    @Value("${services.transaction}")
    private String transactionServiceUrl;

    @Value("${services.card}")
    private String cardServiceUrl;


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

    @Override
    public Mono<Map<String, Object>> getCustomerSummary(String customerId) {
        Mono<Map<String, Object>> customerInfo = webClientBuilder.build()
                .get()
                .uri(customerServiceUrl + "/customers/" + customerId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});

        Mono<List<Map<String, Object>>> bankAccounts = webClientBuilder.build()
                .get()
                .uri(accountServiceUrl + "/bank_accounts/customer/" + customerId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        Mono<List<Map<String, Object>>> credits = webClientBuilder.build()
                .get()
                .uri(creditServiceUrl + "/credits/customer/" + customerId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        return Mono.zip(customerInfo, bankAccounts, credits)
                .map(tuple -> {
                    Map<String, Object> summary = new HashMap<>();
                    summary.put("customerInfo", tuple.getT1());
                    summary.put("bankAccounts", tuple.getT2());
                    summary.put("credits", tuple.getT3());
                    return summary;
                });
    }

    @Override
    public Mono<Map<String, Object>> getGeneralReportByProduct(String customerId, LocalDate dateFrom, LocalDate dateTo) {
        String dateFromStr = UDate.convertToString(dateFrom);
        String dateToStr = UDate.convertToString(dateTo);

        String urlBankAccounts = accountServiceUrl + "/bank_accounts/customer/" + customerId+"?dateFrom="+dateFromStr+"&dateTo="+dateToStr;
        String urlCredits = creditServiceUrl + "/credits/customer/" + customerId+"?dateFrom="+dateFromStr+"&dateTo="+dateToStr;

        Mono<List<Map<String, Object>>> bankAccounts = webClientBuilder.build()
                .get()
                .uri(urlBankAccounts)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        Mono<List<Map<String, Object>>> credits = webClientBuilder.build()
                .get()
                .uri(urlCredits)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        return Mono.zip(bankAccounts, credits)
                .map(tuple -> {
                    Map<String, Object> report = new HashMap<>();
                    report.put("bankAccounts", tuple.getT1());
                    report.put("credits", tuple.getT2());
                    return report;
                });
    }



    /*public Mono<Map<String, List<TransactionDTO>>> getLast10Transactions(String customerId) {
        return webClientBuilder.build()
                .get()
                .uri(transactionServiceUrl + "/transactions/customer/" + customerId)
                .retrieve()
                .bodyToFlux(TransactionDTO.class)
                .filter(transaction -> transaction.getCardType() == Transaction.CardTypeEnum.DEBIT || transaction.getCardType() == Transaction.CardTypeEnum.CREDIT)
                .sort(Comparator.comparing(TransactionDTO::getCreatedAt).reversed())
                .take(10)
                .collectList()
                .map(transactions -> transactions.stream().collect(Collectors.groupingBy(TransactionDTO::getCardType)));
    }
*/


    public Mono<Map<String, List<TransactionDTO>>> getLast10Transactions(String customerId) {
        return webClientBuilder.build()
                .get()
                .uri(transactionServiceUrl + "/transactions/customer/" + customerId)
                .retrieve()
                .bodyToFlux(TransactionDTO.class)
                .filter(transaction -> transaction.getCardType() == Transaction.CardTypeEnum.DEBIT || transaction.getCardType() == Transaction.CardTypeEnum.CREDIT)
                .sort(Comparator.comparing(TransactionDTO::getCreatedAt).reversed())
                .take(10)
                .collectList()
                .map(transactions -> transactions.stream()
                        .collect(Collectors.groupingBy(transaction -> transaction.getCardType().name())));
    }

}