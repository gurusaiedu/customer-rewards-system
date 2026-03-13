package com.quickbuy.rewards.service.impl;

import com.quickbuy.rewards.model.CustomerRewardSummary;
import com.quickbuy.rewards.model.Transaction;
import com.quickbuy.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardServiceImplTest {

    private TransactionRepository transactionRepository;
    private RewardServiceImpl rewardService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        rewardService = new RewardServiceImpl(transactionRepository);

        // Set the private @Value fields via reflection
        setField(rewardService, "tier1Threshold", 50);
        setField(rewardService, "tier1Points", 1);
        setField(rewardService, "tier2Threshold", 100);
        setField(rewardService, "tier2Points", 2);
        setField(rewardService, "numberOfMonths", 3);
    }

    // Utility to set private fields
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCalculateCustomerRewards_empty() {
        // Mock repository to return empty list
        when(transactionRepository.findByTransactionDateAfterOrderByCustomerIdAscTransactionDateAsc(any(LocalDate.class)))
                .thenReturn(List.of());

        List<CustomerRewardSummary> result = rewardService.calculateCustomerRewards();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCalculateCustomerRewards_withTransactions() {
        Transaction t1 = new Transaction(null, "CUST003", 66, LocalDate.of(2026, 1, 10));
        Transaction t2 = new Transaction(null, "CUST003", 66, LocalDate.of(2026, 2, 10));
        Transaction t3 = new Transaction(null, "CUST003", 88, LocalDate.of(2026, 3, 10));
        Transaction t4 = new Transaction(null, "CUST004", 120, LocalDate.of(2026, 3, 15));

        when(transactionRepository.findByTransactionDateAfterOrderByCustomerIdAscTransactionDateAsc(any(LocalDate.class)))
                .thenReturn(List.of(t1, t2, t3, t4));

        List<CustomerRewardSummary> result = rewardService.calculateCustomerRewards();

        assertEquals(2, result.size());

        // Check first customer
        CustomerRewardSummary cust3 = result.stream()
                .filter(c -> c.getCustomerId().equals("CUST003"))
                .findFirst().orElseThrow();

        Map<String, Integer> points3 = cust3.getMonthlyRewardPoints();
        assertEquals(3, points3.size());
        assertEquals(16, points3.get("2026-JANUARY"));
        assertEquals(16, points3.get("2026-FEBRUARY"));
        assertEquals(38, points3.get("2026-MARCH")); // 88 -> (88-50)*1 = 38? Actually 38 points

        // Check second customer
        CustomerRewardSummary cust4 = result.stream()
                .filter(c -> c.getCustomerId().equals("CUST004"))
                .findFirst().orElseThrow();
        assertEquals(1, cust4.getMonthlyRewardPoints().size());
        assertEquals(90, cust4.getMonthlyRewardPoints().get("2026-MARCH")); // (120-100)*2 + (100-50)*1 = 40+50=90
    }

    @Test
    void testCalculatePoints_logic() {
        // Using reflection to access private method
        assertEquals(0, callCalculatePoints(30));
        assertEquals(16, callCalculatePoints(66));
        assertEquals(90, callCalculatePoints(120));
    }

    @Test
    void testCalculateCustomerRewards_databaseException() {
        // Mock repository to throw an exception
        when(transactionRepository.findByTransactionDateAfterOrderByCustomerIdAscTransactionDateAsc(any(LocalDate.class)))
                .thenThrow(new RuntimeException("DB connection error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rewardService.calculateCustomerRewards();
        });

        assertTrue(exception.getMessage().contains("Failed to retrieve transactions from database"));
    }

    private int callCalculatePoints(double amount) {
        try {
            var method = rewardService.getClass().getDeclaredMethod("calculatePoints", double.class);
            method.setAccessible(true);
            return (int) method.invoke(rewardService, amount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}