package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @GetMapping("/api/message")
    public String message() {
        return "Hello from Spring Boot API";
    }
}
