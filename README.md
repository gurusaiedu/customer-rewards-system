# customer-rewards-system
Spring Boot REST API for calculating customer reward points based on retail purchase transactions over a three-month period.

# Rewards Calculation API

## Overview

This Spring Boot application calculates **reward points for customer transactions** based on configurable thresholds.  
The reward rules are **fully configurable via `application.properties`**, making it flexible for future business changes.

### Reward Rules Example

| Purchase Amount | Points per Dollar |
|-----------------|-----------------|
| $50 – $100      | 1               |
| Above $100      | 2               |

---

## Configuration

All thresholds and point values are defined in **`application.properties`**:

```properties
# Tier 1: points start from $50
rewards.tier1.threshold=50
rewards.tier1.points=1

# Tier 2: points start from $100
rewards.tier2.threshold=100
rewards.tier2.points=2


