package org.course.mock;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.course.model.request.BonusSymbol;
import org.course.model.request.Config;
import org.course.model.request.Probabilities;
import org.course.model.request.StandardSymbol;
import org.course.model.request.Symbol;
import org.course.model.request.WinCombination;

public class MockData {
    public static Config getMockConfigWithoutBonusSymbols(){
        Config config = new Config();
        config.setColumns(3);
        config.setRows(1);
        Probabilities probs = new Probabilities();
        StandardSymbol symbolA = getStandardSymbol(0,0, "A");
        StandardSymbol symbolB = getStandardSymbol(0,1, "B");
        StandardSymbol symbolC = getStandardSymbol(0,2, "C");
        List<StandardSymbol> symbols = List.of(symbolA, symbolB, symbolC);
        probs.setStandardSymbols(symbols);
        config.setProbabilities(probs);
        return config;
    }

    public static Config getMockConfigWithBonusSymbols() {
        Config config = new Config();
        config.setColumns(3);
        config.setRows(1);

        Probabilities probs = new Probabilities();
        StandardSymbol symbolA = getStandardSymbol(0,0, "A");
        StandardSymbol symbolB = getStandardSymbol(0,1, "B");
        BonusSymbol bonus = getBonusSymbol("x10");
        List<StandardSymbol> symbols = List.of(symbolA, symbolB);
        probs.setBonusSymbols(bonus);
        probs.setStandardSymbols(symbols);
        config.setProbabilities(probs);
        return config;
    }
    private static StandardSymbol getStandardSymbol(int row, int column, String symbol) {
        StandardSymbol symbolA = new StandardSymbol();
        symbolA.setRow(row);
        symbolA.setColumn(column);
        Map<String, Integer> symbolCombination = new HashMap<>();
        symbolCombination.put(symbol, 1);
        symbolA.setSymbols(symbolCombination);
        return symbolA;
    }
    private static BonusSymbol getBonusSymbol(String symbol) {
        BonusSymbol bonusSymbol = new BonusSymbol();
        bonusSymbol.setBonusSymbols(Map.of(symbol, 1));
        return bonusSymbol;
    }

    public static Config createMockConfig() {
        Config config = new Config();

        Map<String, Symbol> symbols = new HashMap<>();
        symbols.put("A", getSymbol("standard", BigDecimal.valueOf(5), null, null));
        symbols.put("B", getSymbol("standard", BigDecimal.valueOf(3), null, null));
        symbols.put("C", getSymbol("standard",BigDecimal.valueOf(2.5),  null, null));
        symbols.put("10x", getSymbol("bonus", BigDecimal.valueOf(10), "multiply_reward", null));
        symbols.put("5x", getSymbol("bonus", BigDecimal.valueOf(5), "multiply_reward", null));
        symbols.put("+1000", getSymbol("bonus", null, "extra_bonus", BigDecimal.valueOf(1000)));
        symbols.put("+500", getSymbol("bonus", null, "extra_bonus", BigDecimal.valueOf(500)));
        symbols.put("MISS", getSymbol("bonus", null, "miss", null));
        config.setSymbols(symbols);

        Probabilities probabilities = new Probabilities();
        Map<String, Integer> bonusSymbolProbs = new HashMap<>();
        bonusSymbolProbs.put("10x", 1);
        probabilities.setBonusSymbols(new BonusSymbol(bonusSymbolProbs));
        config.setProbabilities(probabilities);
        Map<String, WinCombination> winCombinations = new HashMap<>();

        winCombinations.put("same_symbol_3_times", getWinCombination("same_symbols", BigDecimal.valueOf(1), 3, null));
        winCombinations.put("same_symbol_4_times", getWinCombination("same_symbols", BigDecimal.valueOf(1.5), 4, null));
        winCombinations.put("same_symbol_5_times", getWinCombination("same_symbols", BigDecimal.valueOf(5), 5, null));
        winCombinations.put("same_symbol_6_times", getWinCombination("same_symbols", BigDecimal.valueOf(3), 6, null));
        winCombinations.put("same_symbol_7_times", getWinCombination("same_symbols", BigDecimal.valueOf(5), 7, null));
        winCombinations.put("same_symbol_8_times", getWinCombination("same_symbols", BigDecimal.valueOf(10), 8, null));
        winCombinations.put("same_symbol_9_times", getWinCombination("same_symbols", BigDecimal.valueOf(20), 9, null));

        winCombinations.put("same_symbols_horizontally", getWinCombination("linear_symbols", BigDecimal.valueOf(2), null,
                List.of(
                        List.of("0:0", "0:1", "0:2"),
                        List.of("1:0", "1:1", "1:2"),
                        List.of("2:0", "2:1", "2:2")
                )));
        winCombinations.put("same_symbols_vertically", getWinCombination("linear_symbols", BigDecimal.valueOf(2), null,
                List.of(
                        List.of("0:0", "1:0", "2:0"),
                        List.of("0:1", "1:1", "2:1"),
                        List.of("0:2", "1:2", "2:2")
                )));
        winCombinations.put("same_symbols_diagonally_left_to_right", getWinCombination("linear_symbols", BigDecimal.valueOf(5), null,
                List.of(
                        List.of("0:0", "1:1", "2:2")
                )));
        winCombinations.put("same_symbols_diagonally_right_to_left", getWinCombination("linear_symbols", BigDecimal.valueOf(5), null,
                List.of(
                        List.of("0:2", "1:1", "2:0")
                )));

        config.setWinCombinations(winCombinations);

        return config;
    }

    public static Symbol getSymbol(String type, BigDecimal rewardMultiplier, String impact, BigDecimal extra) {
        Symbol symbol = new Symbol();
        symbol.setRewardMultiplier(rewardMultiplier);
        symbol.setExtra(extra);
        symbol.setType(type);
        symbol.setImpact(impact);
        return symbol;
    }

    public static WinCombination getWinCombination(String when, BigDecimal rewardMultiplier,
                                                   Integer count, List<List<String>> coveredArea){
        WinCombination winCombination = new WinCombination();
        winCombination.setWhen(when);
        winCombination.setCoveredArea(coveredArea);
        winCombination.setRewardMultiplier(rewardMultiplier);
        winCombination.setCount(count);
        return winCombination;
    }

}
