package com.example.expense;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app")
public record AppProperties(String apiKey, Rates rates) {

    public record Rates(String baseUrl, Duration connectTimeout, Duration readTimeout) {
    }

    // Keep the key out of toString() so it can never end up in a log line.
    @Override
    public String toString() {
        return "AppProperties[apiKey=***, rates=" + rates + "]";
    }
}
