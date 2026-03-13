package com.quickbuy.rewards.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRewardSummary {

    private String customerId;
    private Map<String, Integer> monthlyRewardPoints;
    private int totalRewardPoints;
}
