package com.example.demo.module.users.service.impl;

import com.example.demo.entity.Users;
import com.example.demo.module.users.service.UsersService;
import com.example.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    @Override
    public Flux<Users> getUsers() {
        return Flux.merge(
                getUsersSecond(),
                getUsersFirst(),
                getUsersThird()
        );
    }

    @Override
    public Flux<Users> seed() {
//        return userRepository.save(createUser());
        int times = 5;
        List<Users> users = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            users.add(createUser());
        }
        return usersRepository.saveAll(users);
    }

    public Users createUser() {
        Users users = new Users();
        users.setEmail("email" + new Random().nextInt());
        users.setName("name" + new Random().nextInt());

        return users;
    }

    public Flux<Users> getUsersFirst() {
        log.info("getUsersFirst");
        return findOffset(1);
    }

    public Flux<Users> getUsersSecond() {
        log.info("getUsersSecond");
        return Mono.delay(Duration.ofSeconds(2))
                .thenMany(findOffset(2));
    }

    public Flux<Users> getUsersThird() {
        log.info("getUsersThird");
        return Mono.delay(Duration.ofSeconds(3))
                .thenMany(findOffset(3));
    }

    public Flux<Users> findOffset(int offset) {
        return usersRepository.findByOffsetAndLimit(offset, 2);
    }
}
