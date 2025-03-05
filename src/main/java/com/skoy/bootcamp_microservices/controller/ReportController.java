package com.skoy.bootcamp_microservices.controller;

import com.skoy.bootcamp_microservices.service.IReportService;
import com.skoy.bootcamp_microservices.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private IReportService service;


    @GetMapping("/daily-average-balances/{customerId}")
    public Mono<ApiResponse<Map<String, BigDecimal>>> getDailyAverageBalances(@PathVariable String customerId) {
        return service.getDailyAverageBalances(customerId)
                .map(data -> new ApiResponse<>("Success", data, 200));
    }


    @GetMapping("/commissions-by-product")
    public Mono<ApiResponse<Map<String, BigDecimal>>> getCommissionsByProduct(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return service.getCommissionsByProduct(startDate, endDate)
                .map(data -> new ApiResponse<>("Success", data, 200));
    }


}
