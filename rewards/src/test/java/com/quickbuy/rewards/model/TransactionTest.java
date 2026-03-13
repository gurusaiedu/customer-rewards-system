package com.quickbuy.rewards.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testTransactionConstructorAndGetters() {

        LocalDate date = LocalDate.of(2026, 3, 10);

        Transaction transaction = new Transaction(
                1L,
                "CUST001",
                120.50,
                date
        );

        assertEquals(1L, transaction.getId());
        assertEquals("CUST001", transaction.getCustomerId());
        assertEquals(120.50, transaction.getAmount());
        assertEquals(date, transaction.getTransactionDate());
    }

    @Test
    void testSetters() {

        Transaction transaction = new Transaction();

        transaction.setId(2L);
        transaction.setCustomerId("CUST002");
        transaction.setAmount(75.25);
        transaction.setTransactionDate(LocalDate.of(2026, 3, 12));

        assertEquals(2L, transaction.getId());
        assertEquals("CUST002", transaction.getCustomerId());
        assertEquals(75.25, transaction.getAmount());
        assertEquals(LocalDate.of(2026, 3, 12), transaction.getTransactionDate());
    }
    @Test
    void testNoArgsConstructor() {

        Transaction transaction = new Transaction();

        assertNull(transaction.getId());
        assertNull(transaction.getCustomerId());
        assertEquals(0.0, transaction.getAmount());
        assertNull(transaction.getTransactionDate());
    }

    @Test
    void testAllArgsConstructor() {

        LocalDate date = LocalDate.of(2026, 3, 10);

        Transaction transaction =
                new Transaction(1L, "CUST001", 150.0, date);

        assertEquals(1L, transaction.getId());
        assertEquals("CUST001", transaction.getCustomerId());
        assertEquals(150.0, transaction.getAmount());
        assertEquals(date, transaction.getTransactionDate());
    }

    @Test
    void testSettersAndGetters() {

        Transaction transaction = new Transaction();

        transaction.setId(10L);
        transaction.setCustomerId("CUST002");
        transaction.setAmount(200.0);
        transaction.setTransactionDate(LocalDate.of(2026, 3, 11));

        assertEquals(10L, transaction.getId());
        assertEquals("CUST002", transaction.getCustomerId());
        assertEquals(200.0, transaction.getAmount());
        assertEquals(LocalDate.of(2026, 3, 11), transaction.getTransactionDate());
    }

    @Test
    void testEqualsAndHashCode() {

        LocalDate date = LocalDate.of(2026, 3, 10);

        Transaction t1 = new Transaction(1L, "CUST001", 100.0, date);
        Transaction t2 = new Transaction(1L, "CUST001", 100.0, date);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testToString() {

        LocalDate date = LocalDate.of(2026, 3, 10);

        Transaction transaction =
                new Transaction(1L, "CUST001", 120.0, date);

        String result = transaction.toString();

        assertTrue(result.contains("CUST001"));
        assertTrue(result.contains("120.0"));
    }
    @Test
    void testEqualsAndHashCode1() {
        LocalDate date = LocalDate.of(2026, 3, 10);

        Transaction t1 = new Transaction(1L, "CUST001", 100.0, date);
        Transaction t2 = new Transaction(1L, "CUST001", 100.0, date);
        Transaction t3 = new Transaction(2L, "CUST002", 200.0, date);

        // equals
        assertEquals(t1, t2);
        assertNotEquals(t1, t3);

        // hashCode
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1.hashCode(), t3.hashCode());
    }
    @Test
    void testEqualsAndHashCode2() {
        Transaction t1 = new Transaction(1L, "CUST001", 100.0, LocalDate.of(2026,3,10));
        Transaction t2 = new Transaction(1L, "CUST001", 100.0, LocalDate.of(2026,3,10));
        Transaction t3 = new Transaction(2L, "CUST002", 50.0, LocalDate.of(2026,3,11));

        // equals
        assertEquals(t1, t2);
        assertNotEquals(t1, t3);

        // hashCode
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1.hashCode(), t3.hashCode());
    }

    @Test
    void testToStringAndGetters() {
        Transaction t = new Transaction(1L, "CUST001", 100.0, LocalDate.of(2026,3,10));

        assertNotNull(t.toString());
        assertEquals("CUST001", t.getCustomerId());
        assertEquals(100.0, t.getAmount());
        assertEquals(LocalDate.of(2026,3,10), t.getTransactionDate());
    }

    @Test
    void testAllLombokMethods() {
        // Prepare test data
        Map<String, Integer> monthlyPoints = new HashMap<>();
        monthlyPoints.put("MARCH", 50);

        // Test constructors
        CustomerRewardSummary summary1 = new CustomerRewardSummary("CUST001", monthlyPoints, 50);
        CustomerRewardSummary summary2 = new CustomerRewardSummary();
        summary2.setCustomerId("CUST001");
        summary2.setMonthlyRewardPoints(monthlyPoints);
        summary2.setTotalRewardPoints(50);

        // Test getters
        assertEquals("CUST001", summary1.getCustomerId());
        assertEquals(monthlyPoints, summary1.getMonthlyRewardPoints());
        assertEquals(50, summary1.getTotalRewardPoints());

        // Test equals & hashCode
        assertEquals(summary1, summary2);
        assertEquals(summary1.hashCode(), summary2.hashCode());

        // Test toString
        assertNotNull(summary1.toString());
    }

}