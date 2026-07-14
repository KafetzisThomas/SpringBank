<div align="center">
    <img src="src/main/resources/static/favicon.ico" width="150"/>
    <p>A simple banking system concept.<br>Written in Java/Spring Boot</p>
</div>

## Features

- Deposit and withdraw money
- View transaction history with an optional date range filter
- See real-time balance updates after each transaction

## Tech Stack

Built with Java 25, Spring Boot, MySQL, Thymeleaf and Bootstrap 5.

## Database Schema

![Database Schema](assets/springbank_db_schema.png)

## Usage

Start the PostgreSQL database:

```bash
docker compose up -d
```

Run the Spring Boot application:

```bash
# RECOMMENDED: PowerShell/Linux/Mac
./mvnw spring-boot:run

# Windows CMD
.\mvnw.cmd spring-boot:run
```

Access web application at http://127.0.0.1:8080 or http://localhost:8080.

## Run Tests

```bash
./mvnw test
```

## Demo Images

![Deposit Transaction](assets/deposit_transaction.png)

![Withdraw Transaction](assets/withdraw_transaction.png)

![Date Range Filter](assets/date_range_filter.png)
