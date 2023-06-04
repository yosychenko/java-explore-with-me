package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.stats.gateway"})
public class EWMService {
    public static void main(String[] args) {
        SpringApplication.run(EWMService.class, args);
    }
}