package org.course.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.course.model.request.Config;
import org.course.model.request.Symbol;
import org.course.model.request.WinCombination;
import org.course.model.response.Response;
import org.course.service.RewardCalculator;

public class RewardCalculatorImpl implements RewardCalculator {

    public static final String SAME_SYMBOLS = "same_symbols";
    public static final String LINEAR_SYMBOLS = "linear_symbols";

    @Override
    public Response calculateReward(Response response, Config config, BigDecimal bet) {
        Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
        Map<String, Symbol> symbols = config.getSymbols();

        List<String> standardSymbols = getStandardSymbolsByType(symbols, "standard");
        List<String> appliedBonusSymbols = getStandardSymbolsByType(symbols, "bonus");

        String[][] matrix = response.getMatrix();
        Map<String, Integer> symbolCountMap = countSymbols(matrix);

        List<String> bonuses = filterBonusSymbols(symbolCountMap, appliedBonusSymbols);

        applyWinCombinations(matrix, config, symbolCountMap, standardSymbols, appliedWinningCombinations);

        response.setAppliedWinningCombinations(appliedWinningCombinations);

        if (!appliedWinningCombinations.isEmpty() && !bonuses.isEmpty()) {
            String bonusSymbols = extractBonusSymbols(matrix, bonuses);
            response.setAppliedBonusSymbol(bonusSymbols);
        }
        BigDecimal rewardAmount = calculateRewardAmount(bet, appliedWinningCombinations, bonuses, config);
        response.setReward(rewardAmount);
        return response;
    }

    private BigDecimal calculateRewardAmount(BigDecimal bet,
                                         Map<String, List<String>> appliedWinningCombinations,
                                         List<String> bonus,
                                         Config config) {
        if (appliedWinningCombinations.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalMultiplier = calculateTotalMultiplier(appliedWinningCombinations, config);
        BigDecimal reward = bet.multiply(totalMultiplier);

        return applyBonusEffects(reward, bonus, config);

    }

    private BigDecimal calculateTotalMultiplier(Map<String, List<String>> appliedWinningCombinations, Config config) {
        BigDecimal totalMultiplier = BigDecimal.ZERO;

        for (Map.Entry<String, List<String>> entry : appliedWinningCombinations.entrySet()) {
            String symbol = entry.getKey();
            List<String> combinations = entry.getValue();

            BigDecimal combinationMultiplier = config.getSymbols().get(symbol).getRewardMultiplier();
            for (String combination : combinations) {
                BigDecimal winMultiplier = config.getWinCombinations().get(combination).getRewardMultiplier();
                combinationMultiplier = combinationMultiplier.multiply(winMultiplier);
            }

            totalMultiplier = totalMultiplier.add(combinationMultiplier);
        }

        return totalMultiplier;
    }

    private BigDecimal applyBonusEffects(BigDecimal reward, List<String> bonus, Config config) {

        reward = reward.multiply(getMultiplyBonusReward(bonus, config));
        reward = reward.add(getExtraBonusesReward(bonus, config));

        return reward;
    }

    private static BigDecimal getMultiplyBonusReward(List<String> bonus, Config config) {
        BigDecimal multiplier = BigDecimal.ONE;
        List<BigDecimal> rewardMultipliers = bonus.stream()
                .map(symbol -> config.getSymbols().get(symbol))
                .filter(symbol -> "multiply_reward".equals(symbol.getImpact()))
                .map(Symbol::getRewardMultiplier)
                .toList();

        for (BigDecimal mult : rewardMultipliers) {
            multiplier = multiplier.multiply(mult);
        }
        return multiplier;
    }

    private BigDecimal getExtraBonusesReward(List<String> bonus, Config config) {
        return bonus.stream()
                .map(symbol -> config.getSymbols().get(symbol))
                .filter(symbol -> "extra_bonus".equals(symbol.getImpact()))
                .map(Symbol::getExtra)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<String> filterBonusSymbols(Map<String, Integer> symbolCountMap, List<String> appliedBonusSymbols) {
        return symbolCountMap.keySet().stream()
                .filter(appliedBonusSymbols::contains)
                .toList();
    }

    private void applyWinCombinations(String[][] matrix, Config config, Map<String, Integer> symbolCountMap,
                                      List<String> standardSymbols, Map<String, List<String>> appliedWinningCombinations) {
        config.getWinCombinations().forEach((type, winCombination) -> {
            switch (winCombination.getWhen()) {
                case SAME_SYMBOLS -> calcSameSymbolGroupReward(symbolCountMap, type,
                        winCombination, appliedWinningCombinations, standardSymbols);
                case LINEAR_SYMBOLS -> calcLinearSymbolGroupReward(matrix, type,
                        winCombination, appliedWinningCombinations);
            }
        });
    }

    private void calcLinearSymbolGroupReward(String[][] matrix, String type, WinCombination winCombination,
                                             Map<String, List<String>> appliedWinningCombinations) {

        winCombination.getCoveredArea()
                .forEach(x -> {
                    Set<String> line = new HashSet<>();
                    for (String ele: x){
                        String[] indexes = ele.split(":");
                        int i = Integer.parseInt(indexes[0]);
                        int j = Integer.parseInt(indexes[1]);
                        line.add(matrix[i][j]);
                    }
                    if (line.size() == 1) {
                        String symbol = line.iterator().next();
                        appliedWinningCombinations
                                .computeIfAbsent(symbol, key -> new ArrayList<>())
                                .add(type);
                    }
                });
    }

    private String extractBonusSymbols(String[][] matrix, List<String> bonuses) {
        return Arrays.stream(matrix)
                .flatMap(rows -> Arrays.stream(rows)
                .filter(bonuses::contains))
                .findFirst()
                .orElse(null);
    }

    private Map<String, Integer> countSymbols(String[][] matrix) {
        Map<String, Integer> symbolCountMap = new HashMap<>();

        for (String[] row : matrix) {
            for (String element : row) {
                symbolCountMap.put(element, symbolCountMap.getOrDefault(element, 0) + 1);
            }
        }
        return symbolCountMap;
    }

    private static List<String> getStandardSymbolsByType(Map<String, Symbol> symbols, String symbolType) {
        return symbols.entrySet().stream()
                .filter(stringSymbolEntry -> symbolType.equals(stringSymbolEntry.getValue().getType()))
                .filter(entry -> !"miss".equals(entry.getValue().getImpact()))
                .map(Map.Entry::getKey)
                .toList();
    }

    private void calcSameSymbolGroupReward(Map<String, Integer> matrix,
                                           String type, WinCombination config,
                                           Map<String, List<String>> rewardMatrix, List<String> standardSymbols) {
        matrix.forEach((key, value) -> {
            if (standardSymbols.contains(key)){
                if (value.equals(config.getCount())) {
                    List<String> list =
                            rewardMatrix.getOrDefault(key, new ArrayList<>());
                    list.add(type);
                    rewardMatrix.put(key, list);
                }
            }
        });
    }
}
