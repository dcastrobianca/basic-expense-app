# expense-api

A small expense service. `POST /expenses` stores an expense and its amount converted to EUR using the current rate from [Frankfurter](https://frankfurter.dev). `GET /expenses` lists the stored expenses.

## Requirements

- Java 25 (LTS). On the Ubuntu VM, check what apt offers first: `apt-cache search --names-only '^openjdk-[0-9]+-jdk-headless$'`, then install it with `sudo apt install openjdk-25-jdk-headless`.
- Maven 3.9 or later: `sudo apt install maven`
- PostgreSQL with an empty database and a user that owns it. Flyway creates the table on first start.

## Configuration

All settings are in `src/main/resources/application.yml`. Each one can be overridden by an environment variable.

| Variable | Default | Purpose |
|---|---|---|
| `SERVER_PORT` | `8080` | HTTP port |
| `DB_URL` | `jdbc:postgresql://localhost:5432/expense_app_db` | JDBC URL of the database |
| `DB_USER` | `expense_app` | Database user |
| `DB_PASSWORD_EXPENSE` | *(none)* | Database password. **Secret** |
| `EXPENSE_API_KEY` | *(none, required)* | Value clients must send in `X-API-Key`. The app will not start without it. **Secret** |
| `RATES_BASE_URL` | `https://api.frankfurter.dev/v1` | Base URL of the exchange-rate API |
| `RATES_CONNECT_TIMEOUT` | `3s` | Connect timeout for the rate API |
| `RATES_READ_TIMEOUT` | `5s` | Read timeout for the rate API |

Timeouts use Spring duration syntax, for example `500ms`, `3s` or `1m`.

Secrets have no defaults and are never logged. Keep them in an environment file outside the repository, not in `application.yml`. Do not put the password inside `DB_URL`.

## Build

```sh
mvn package
```

This produces `target/expense-api.jar`.

## Run

```sh
export DB_PASSWORD_EXPENSE='...'
export EXPENSE_API_KEY='...'
java -jar target/expense-api.jar
```

Each request is logged as one line on standard output, for example `POST /expenses 201 412ms`.

## Endpoints

| Method and path | Auth | Description |
|---|---|---|
| `POST /expenses` | `X-API-Key` | Body: `{"description": "Lunch", "amount": 12.50, "currency": "USD"}`. Currency must be `EUR`, `GBP`, `USD` or `BRL`. Returns `201` with the stored row |
| `GET /expenses` | `X-API-Key` | Lists all stored expenses |
| `GET /actuator/health` | none | Application and database status |

Status codes: `401` when the key is missing or wrong, `400` for invalid input, `502` when the rate API fails or times out.

```sh
curl -X POST localhost:8080/expenses -H "X-API-Key: $EXPENSE_API_KEY" \
     -H 'Content-Type: application/json' \
     -d '{"description":"Lunch","amount":12.50,"currency":"USD"}'
curl localhost:8080/expenses -H "X-API-Key: $EXPENSE_API_KEY"
curl localhost:8080/actuator/health
```
