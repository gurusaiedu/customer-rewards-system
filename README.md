# customer-rewards-system
Spring Boot REST API for calculating customer reward points based on retail purchase transactions over a three-month period.

# Rewards Calculation API

## Overview

This Spring Boot application calculates **reward points for customer transactions** based on configurable thresholds and configurable period (number of months).  

The reward rules are **fully configurable via `application.properties`**, making it flexible for future business changes.  

### Reward Rules Example

| Purchase Amount | Points per Dollar |
|-----------------|-----------------|
| $50 – $100      | 1               |
| Above $100      | 2               |

---

## Configuration

All thresholds, points, and period are defined in **`application.properties`**:

```properties
# Tier 1: points start from $50
rewards.tier1.threshold=50
rewards.tier1.points=1

# Tier 2: points start from $100
rewards.tier2.threshold=100
rewards.tier2.points=2

# Number of months to calculate rewards (e.g., last 3 months)
rewards.numberOfMonths=3
---
### Project Structure

src/main/java
└── com.quickbuy.rewards
    ├── controller
    │   └── RewardController.java
    ├── model
    │   ├── CustomerRewardSummary.java
    │   └── Transaction.java
    ├── repository
    │   └── TransactionRepository.java
    ├── service
    │   └── RewardService.java
    └── service/impl
        └── RewardServiceImpl.java


---
### Package Descriptions

- **controller** – Handles API requests and endpoints.
  - `RewardController.java` – Exposes REST APIs for fetching customer rewards and summaries.

- **model** – Contains entity and DTO classes.
  - `Transaction.java` – Represents a customer transaction with fields like transactionId, customerId, amount, and transactionDate.
  - `CustomerRewardSummary.java` – Represents reward summary per customer per month, including total reward points.

- **repository** – Provides data access layer using JPA.
  - `TransactionRepository.java` – Repository interface for querying transaction data from the database.

- **service** – Defines the business logic interface for reward calculation.
  - `RewardService.java` – Interface that declares methods to calculate reward points.

- **service.impl** – Contains the implementation of service interfaces.
  - `RewardServiceImpl.java` – Implements reward calculation logic based on transaction data.

---

## Features

- Calculate reward points for each transaction based on amount spent.
- Generate monthly reward summary for each customer.
- Provide total reward points for all months.
- REST APIs for accessing rewards data.

---

## API Endpoints

### Base Endpoint
**URL:** `http://localhost:8080/api/rewards`

**Method:** `GET`

**Sample Response:**
```json
[
  {
    "customerId": "CUST003",
    "monthlyRewardPoints": {
      "2026-JANUARY": 54,
      "2026-FEBRUARY": 16,
      "2026-MARCH": 16
    },
    "totalRewardPoints": 102
  }
]

---

## How to Run

### Step 1: Configure `application.properties`
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/rewardsdb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Reward Configuration
rewards.numberOfMonths=3
rewards.threshold1=50
rewards.threshold2=100
rewards.point1=1
rewards.point2=2

### Step 2: Build the Project`

mvn clean install

### Step 3: Run the Spring Boot Application`

http://localhost:8080/api/rewards

### Step 4: Access API`

http://localhost:8080/api/rewards

---
## Future Enhancements

- Add pagination & sorting for all customers
- Caching reward calculations for performance
- Add export reports to CSV / Excel
- Add Swagger UI documentation for easy testing
- Support dynamic tiers from configuration beyond 2 tiers
- Add alerts/notifications for reward milestones
- Total reward points for all customers in a specific period
- Rewards for one customer in a specific period
