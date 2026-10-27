package com.example.expense;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

/** Fetches the current rate to EUR from Frankfurter. */
@Component
public class RateClient {

    private static final Logger log = LoggerFactory.getLogger(RateClient.class);

    private record RateResponse(Map<String, BigDecimal> rates) {
    }

    private final RestClient restClient;

    public RateClient(AppProperties props) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(props.rates().connectTimeout())
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(props.rates().readTimeout());
        this.restClient = RestClient.builder()
                .baseUrl(props.rates().baseUrl())
                .requestFactory(factory)
                .build();
    }

    public BigDecimal rateToEur(Currency currency) {
        if (currency == Currency.EUR) {
            return BigDecimal.ONE;
        }
        try {
            RateResponse response = restClient.get()
                    .uri("/latest?base={base}&symbols=EUR", currency.name())
                    .retrieve()
                    .body(RateResponse.class);
            if (response == null || response.rates() == null || response.rates().get("EUR") == null) {
                throw new RestClientException("response has no EUR rate");
            }
            return response.rates().get("EUR");
        } catch (RestClientException e) {
            log.warn("Rate lookup failed for {}: {}", currency, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Exchange rate service unavailable");
        }
    }
}
