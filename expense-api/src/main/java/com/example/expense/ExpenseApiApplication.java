package com.example.expense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ExpenseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseApiApplication.class, args);
    }
}
