package com.quickbuy.rewards.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRewardSummaryTest {

    @Test
    void testNoArgsConstructor() {
        CustomerRewardSummary summary = new CustomerRewardSummary();

        assertNull(summary.getCustomerId());
        assertNull(summary.getMonthlyRewardPoints());
        assertEquals(0, summary.getTotalRewardPoints());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        Map<String, Integer> rewards = new HashMap<>();
        rewards.put("March", 120);
        rewards.put("April", 150);

        CustomerRewardSummary summary =
                new CustomerRewardSummary("CUST001", rewards, 270);

        assertEquals("CUST001", summary.getCustomerId());
        assertEquals(2, summary.getMonthlyRewardPoints().size());
        assertEquals(270, summary.getTotalRewardPoints());
        assertEquals(120, summary.getMonthlyRewardPoints().get("March"));
    }

    @Test
    void testSetters() {
        CustomerRewardSummary summary = new CustomerRewardSummary();

        Map<String, Integer> rewards = new HashMap<>();
        rewards.put("May", 200);

        summary.setCustomerId("CUST002");
        summary.setMonthlyRewardPoints(rewards);
        summary.setTotalRewardPoints(200);

        assertEquals("CUST002", summary.getCustomerId());
        assertEquals(1, summary.getMonthlyRewardPoints().size());
        assertEquals(200, summary.getTotalRewardPoints());
    }

    @Test
    void testEqualsAndHashCode() {
        Map<String, Integer> rewards1 = new HashMap<>();
        rewards1.put("March", 100);

        Map<String, Integer> rewards2 = new HashMap<>();
        rewards2.put("March", 100);

        CustomerRewardSummary s1 = new CustomerRewardSummary("CUST001", rewards1, 100);
        CustomerRewardSummary s2 = new CustomerRewardSummary("CUST001", rewards2, 100);
        CustomerRewardSummary s3 = new CustomerRewardSummary("CUST002", rewards2, 100);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1, s3);
        assertNotEquals(s1.hashCode(), s3.hashCode());
    }

    @Test
    void testToString() {
        Map<String, Integer> rewards = new HashMap<>();
        rewards.put("March", 120);

        CustomerRewardSummary summary = new CustomerRewardSummary("CUST001", rewards, 120);

        String str = summary.toString();

        assertTrue(str.contains("CUST001"));
        assertTrue(str.contains("120"));
    }
}