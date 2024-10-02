package com.example.demo.controller;

import com.example.demo.entity.Users;
import com.example.demo.module.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("v1")
@RequiredArgsConstructor
public class DemoController {
    private final UsersService usersService;

    @GetMapping(path = "demo", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Users> getDemo() {
        return usersService.getUsers();
    }

    @PostMapping("seed")
    public Flux<Users> seed() {
        return usersService.seed();
    }
}
