package com.skoy.bootcamp_microservices.repository;


import com.skoy.bootcamp_microservices.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IReportRepository extends ReactiveMongoRepository<Transaction, String> {
    Flux<Transaction> findByCustomerId(String customerId);
}
