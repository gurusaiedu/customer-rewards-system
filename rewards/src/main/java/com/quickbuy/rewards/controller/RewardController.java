package com.quickbuy.rewards.controller;


import com.quickbuy.rewards.model.CustomerRewardSummary;
import com.quickbuy.rewards.service.RewardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerRewardSummary> > getCustomerRewards() {
        log.info("Fetching customer reward summaries for the last three months.");

        List<CustomerRewardSummary>  finalResponse= rewardService.calculateCustomerRewards();
        if (finalResponse.isEmpty()) {
            log.info("No reward data found for the requested period.");
            return ResponseEntity.ok(finalResponse);
        }
        return ResponseEntity.ok(finalResponse);
    }
}