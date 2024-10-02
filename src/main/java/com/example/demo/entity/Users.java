package com.example.demo.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@Document(collection = "users")
public class Users {
    @Id
    private UUID id;
    private String name;
    private String email;
}
