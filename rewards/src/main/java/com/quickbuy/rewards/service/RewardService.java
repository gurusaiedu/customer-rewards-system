package com.quickbuy.rewards.service;


import com.quickbuy.rewards.model.CustomerRewardSummary;

import java.util.List;

public interface RewardService {
    List<CustomerRewardSummary> calculateCustomerRewards();

    CustomerRewardSummary getCustomerReward(String customerId);
}
