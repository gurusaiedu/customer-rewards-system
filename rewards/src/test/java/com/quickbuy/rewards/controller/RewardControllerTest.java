package com.quickbuy.rewards.controller;

import com.quickbuy.rewards.model.CustomerRewardSummary;
import com.quickbuy.rewards.service.RewardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @Test
    void testGetCustomerRewardsEmptyList() throws Exception {
        when(rewardService.calculateCustomerRewards())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/rewards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetCustomerRewardsWithData() throws Exception {
        Map<String, Integer> monthlyPoints = new HashMap<>();
        monthlyPoints.put("MARCH", 50);
        monthlyPoints.put("APRIL", 70);

        CustomerRewardSummary summary = new CustomerRewardSummary("CUST001", monthlyPoints, 120);

        when(rewardService.calculateCustomerRewards())
                .thenReturn(List.of(summary));

        mockMvc.perform(get("/api/rewards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerId", is("CUST001")))
                .andExpect(jsonPath("$[0].totalRewardPoints", is(120)))
                .andExpect(jsonPath("$[0].monthlyRewardPoints.MARCH", is(50)))
                .andExpect(jsonPath("$[0].monthlyRewardPoints.APRIL", is(70)));
    }
}