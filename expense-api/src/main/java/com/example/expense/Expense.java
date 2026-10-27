package com.example.expense;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private BigDecimal rate;

    private BigDecimal amountEur;

    private Instant createdAt;

    protected Expense() {
    }

    public Expense(String description, BigDecimal amount, Currency currency, BigDecimal rate, BigDecimal amountEur) {
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.rate = rate;
        this.amountEur = amountEur;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public Currency getCurrency() { return currency; }
    public BigDecimal getRate() { return rate; }
    public BigDecimal getAmountEur() { return amountEur; }
    public Instant getCreatedAt() { return createdAt; }
}
