package com.example.expense;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    public record NewExpense(String description, BigDecimal amount, Currency currency) {
    }

    private final ExpenseRepository repository;
    private final RateClient rateClient;

    public ExpenseController(ExpenseRepository repository, RateClient rateClient) {
        this.repository = repository;
        this.rateClient = rateClient;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expense create(@RequestBody NewExpense body) {
        if (body.description() == null || body.description().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description is required");
        }
        if (body.amount() == null || body.amount().signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount must be positive");
        }
        if (body.currency() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "currency must be EUR, GBP, USD or BRL");
        }
        BigDecimal rate = rateClient.rateToEur(body.currency());
        BigDecimal amountEur = body.amount().multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
        return repository.save(new Expense(body.description(), body.amount(), body.currency(), rate, amountEur));
    }

    @GetMapping
    public List<Expense> list() {
        return repository.findAll();
    }
}
