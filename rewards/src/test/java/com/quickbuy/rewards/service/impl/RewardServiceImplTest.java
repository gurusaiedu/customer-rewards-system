package com.quickbuy.rewards.service.impl;

import com.quickbuy.rewards.model.CustomerRewardSummary;
import com.quickbuy.rewards.model.Transaction;
import com.quickbuy.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardServiceImplTest {

    private TransactionRepository transactionRepository;
    private RewardServiceImpl rewardService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        rewardService = new RewardServiceImpl(transactionRepository);

        // Set private @Value fields via ReflectionTestUtils
        ReflectionTestUtils.setField(rewardService, "tier1Threshold", 50);
        ReflectionTestUtils.setField(rewardService, "tier1Points", 1);
        ReflectionTestUtils.setField(rewardService, "tier2Threshold", 100);
        ReflectionTestUtils.setField(rewardService, "tier2Points", 2);
    }

    @Test
    void testCalculateCustomerRewardsEmpty() {
        when(transactionRepository.findAllByOrderByCustomerIdAscTransactionDateAsc())
                .thenReturn(Collections.emptyList());

        List<CustomerRewardSummary> result = rewardService.calculateCustomerRewards();
        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1)).findAllByOrderByCustomerIdAscTransactionDateAsc();
    }

    @Test
    void testTier1PointsIndirectly() {
        Transaction t1 = new Transaction(null, "CUST001", 70, LocalDate.of(2026, 3, 10));
        when(transactionRepository.findAllByOrderByCustomerIdAscTransactionDateAsc())
                .thenReturn(Collections.singletonList(t1));

        List<CustomerRewardSummary> result = rewardService.calculateCustomerRewards();

        CustomerRewardSummary summary = result.get(0);
        assertEquals(20, summary.getTotalRewardPoints()); // (70-50)*1 = 20
    }

    @Test
    void testTier2PointsIndirectly() {
        Transaction t1 = new Transaction(null, "CUST001", 120, LocalDate.of(2026, 3, 10));
        when(transactionRepository.findAllByOrderByCustomerIdAscTransactionDateAsc())
                .thenReturn(Collections.singletonList(t1));

        List<CustomerRewardSummary> result = rewardService.calculateCustomerRewards();

        CustomerRewardSummary summary = result.get(0);
        assertEquals(90, summary.getTotalRewardPoints()); // (120-100)*2 + (100-50)*1 = 90
    }

    @Test
    void testMultipleMonthsSingleCustomer() {
        Transaction t1 = new Transaction(null, "CUST001", 70, LocalDate.of(2026,3,10));
        Transaction t2 = new Transaction(null, "CUST001", 120, LocalDate.of(2026,4,5));

        when(transactionRepository.findAllByOrderByCustomerIdAscTransactionDateAsc())
                .thenReturn(Arrays.asList(t1, t2));

        List<CustomerRewardSummary> result = rewardService.calculateCustomerRewards();

        CustomerRewardSummary summary = result.get(0);
        assertEquals(110, summary.getTotalRewardPoints());
        assertEquals(2, summary.getMonthlyRewardPoints().size());
        assertEquals(20, summary.getMonthlyRewardPoints().get("MARCH"));
        assertEquals(90, summary.getMonthlyRewardPoints().get("APRIL"));
    }

    @Test
    void testMultipleCustomers() {
        Transaction t1 = new Transaction(null, "CUST001", 70, LocalDate.of(2026,3,10));
        Transaction t2 = new Transaction(null, "CUST002", 120, LocalDate.of(2026,4,5));

        when(transactionRepository.findAllByOrderByCustomerIdAscTransactionDateAsc())
                .thenReturn(Arrays.asList(t1, t2));

        List<CustomerRewardSummary> result = rewardService.calculateCustomerRewards();

        assertEquals(2, result.size());
        assertEquals(90, result.get(0).getTotalRewardPoints());
        assertEquals(20, result.get(1).getTotalRewardPoints());
    }

    @Test
    void testDatabaseException() {
        when(transactionRepository.findAllByOrderByCustomerIdAscTransactionDateAsc())
                .thenThrow(new RuntimeException("DB Error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rewardService.calculateCustomerRewards());

        assertTrue(ex.getMessage().contains("Failed to retrieve transactions"));
    }

}