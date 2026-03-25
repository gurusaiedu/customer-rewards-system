package com.quickbuy.rewards.service.impl;

import com.quickbuy.rewards.exception.ResourceNotFoundException;
import com.quickbuy.rewards.model.CustomerRewardSummary;
import com.quickbuy.rewards.model.Transaction;
import com.quickbuy.rewards.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import com.quickbuy.rewards.service.RewardService;

import java.time.Month;
import java.util.*;

@Slf4j
@Service
public class RewardServiceImpl implements RewardService {

    @Value("${rewards.tier1.threshold}")
    private int tier1Threshold;

    @Value("${rewards.tier1.points}")
    private int tier1Points;

    @Value("${rewards.tier2.threshold}")
    private int tier2Threshold;

    @Value("${rewards.tier2.points}")
    private int tier2Points;

    @Value("${rewards.numberOfMonths}")
    private int numberOfMonths;

    private final TransactionRepository transactionRepository;

    public RewardServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<CustomerRewardSummary> calculateCustomerRewards() {
        log.info("Starting calculation of customer rewards.");
        List<Transaction> transactions;
        try {

            LocalDate threeMonthsAgo = LocalDate.now().minusMonths(numberOfMonths);
            transactions = transactionRepository.findByTransactionDateAfterOrderByCustomerIdAscTransactionDateAsc(threeMonthsAgo);
        } catch (Exception e) {
            log.error("Database error occurred while fetching transactions: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve transactions from database", e);
        }

        if (transactions.isEmpty()) {
            log.info("No transactions found in the database. Returning empty summary list.");
            return Collections.emptyList();
        }

        log.info("Processing {} transactions to calculate rewards.", transactions.size());
        Map<String, Map<String, Integer>> rewardTracker = new HashMap<>();

        for (Transaction t : transactions) {
            String customerId = t.getCustomerId();
            String month = t.getTransactionDate().getYear() + "-" + t.getTransactionDate().getMonth().toString();

            int points = calculatePoints(t.getAmount());

            rewardTracker
                    .computeIfAbsent(customerId, k -> new HashMap<>())
                    .merge(month, points, Integer::sum);
        }

        List<CustomerRewardSummary> summaries = new ArrayList<>();
        for (String customer : rewardTracker.keySet()) {

            Map<String, Integer> monthlyPoints = rewardTracker.get(customer);
            int totalPoints = monthlyPoints.values().stream().mapToInt(Integer::intValue).sum();
            summaries.add(new CustomerRewardSummary(customer, monthlyPoints, totalPoints));
            log.info("Summary created for Customer: {} | Total Points: {}", customer, totalPoints);
        }
        log.info("Successfully calculated rewards for {} customers.", summaries.size());
        log.info("Final response : {}", summaries);
        return summaries;
    }

    private int calculatePoints(double amount) {

        int points = 0;
        if (amount > tier2Threshold) {
            // points above tier2 threshold
            points += (amount - tier2Threshold) * tier2Points;
            // points between tier1 and tier2
            points += (tier2Threshold - tier1Threshold) * tier1Points;
        }
        else if (amount > tier1Threshold) {
            // points between tier1 threshold and purchase amount
            points += (amount - tier1Threshold) * tier1Points;
        }
        return points;
    }

    @Override
    public CustomerRewardSummary getCustomerReward(String customerId) {

        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(numberOfMonths);

        List<Transaction> transactions = transactionRepository
                .findByCustomerIdAndTransactionDateAfter(customerId, threeMonthsAgo);

        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found for customer: " + customerId);
        }

        Map<String, Integer> monthlyPoints = new HashMap<>();

        for (Transaction t : transactions) {
            String month = t.getTransactionDate().getYear() + "-" + t.getTransactionDate().getMonth();
            int points = calculatePoints(t.getAmount());

            monthlyPoints.merge(month, points, Integer::sum);
        }

        int totalPoints = monthlyPoints.values().stream().mapToInt(Integer::intValue).sum();

        return new CustomerRewardSummary(customerId, monthlyPoints, totalPoints);
    }
}
