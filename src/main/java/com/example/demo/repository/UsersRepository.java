package com.example.demo.repository;

import com.example.demo.entity.Users;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface UsersRepository extends ReactiveMongoRepository<Users, UUID> {
    @Query("SELECT * FROM users ORDER BY id LIMIT :limit OFFSET :offset")
    Flux<Users> findByOffsetAndLimit(int offset, int limit);

}
