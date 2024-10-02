package com.example.demo.repository;

import com.example.demo.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrdersRepository extends ReactiveMongoRepository<Orders, String> {
    Flux<Orders> findAllBy(Pageable pageable);
    Mono<Orders> findByInvoiceNumberEqualsIgnoreCase(String invoiceNumber);


}
