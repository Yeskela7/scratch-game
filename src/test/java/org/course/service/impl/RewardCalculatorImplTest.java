package org.course.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import org.course.mock.MockData;
import org.course.model.request.Config;
import org.course.model.response.Response;
import org.junit.jupiter.api.Test;

class RewardCalculatorImplTest {

    @Test
    void testCalculateRewardWinWithPlusBonus() {
        Config config = MockData.createMockConfig();
        RewardCalculatorImpl rewardCalculator = new RewardCalculatorImpl();

        String[][] matrix = {
                {"A", "A", "B"},
                {"A", "+1000", "B"},
                {"A", "A", "B"}
        };

        Response response = new Response();
        response.setMatrix(matrix);
        BigDecimal bet = BigDecimal.valueOf(100);
        rewardCalculator.calculateReward(response, config, bet);

        System.out.println(response.getAppliedWinningCombinations());
        System.out.println("Reward: " + response.getReward());
        assertNotNull(response.getReward());
        assertEquals(0, response.getReward().compareTo(BigDecimal.valueOf(6600)));
    }

    @Test
    void testCalculateRewardWinWithMultiplyBonus() {
        Config config = MockData.createMockConfig();
        RewardCalculatorImpl rewardCalculator = new RewardCalculatorImpl();

        String[][] matrix = {
                {"A", "A", "B"},
                {"A", "10x", "B"},
                {"A", "A", "B"}
        };

        Response response = new Response();
        response.setMatrix(matrix);
        BigDecimal bet = BigDecimal.valueOf(100);
        rewardCalculator.calculateReward(response, config, bet);

        assertNotNull(response.getReward());
        assertEquals(0, response.getReward().compareTo(BigDecimal.valueOf(56000)));
        System.out.println("Reward: " + response.getReward());
    }

    @Test
    void testCalculateRewardLost() {
        Config config = MockData.createMockConfig();
        RewardCalculatorImpl rewardCalculator = new RewardCalculatorImpl();

        String[][] matrix = {
                {"A", "B", "C"},
                {"E", "B", "5x"},
                {"F", "D", "C"}
        };

        Response response = new Response();
        response.setMatrix(matrix);
        BigDecimal bet = BigDecimal.valueOf(100);
        rewardCalculator.calculateReward(response, config, bet);

        assertNotNull(response.getReward());
        assertEquals(0, response.getReward().compareTo(BigDecimal.ZERO));
        System.out.println("Reward: " + response.getReward());
    }
}