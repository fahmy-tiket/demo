package com.example.demo.module.users.service;

import com.example.demo.entity.Users;
import reactor.core.publisher.Flux;

public interface UsersService {
    Flux<Users> getUsers();
    Flux<Users> seed();
}
