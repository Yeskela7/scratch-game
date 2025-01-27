package org.course.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Probabilities {
    @JsonProperty("standard_symbols")
    private List<StandardSymbol> standardSymbols;
    @JsonProperty("bonus_symbols")
    private BonusSymbol bonusSymbols;
    @JsonProperty("win_combinations")
    private List<WinCombination> winCombinations;

    public List<StandardSymbol> getStandardSymbols() {
        return standardSymbols;
    }

    public void setStandardSymbols(List<StandardSymbol> standardSymbols) {
        this.standardSymbols = standardSymbols;
    }

    public BonusSymbol getBonusSymbols() {
        return bonusSymbols;
    }

    public void setBonusSymbols(BonusSymbol bonusSymbols) {
        this.bonusSymbols = bonusSymbols;
    }

    public List<WinCombination> getWinCombinations() {
        return winCombinations;
    }

    public void setWinCombinations(List<WinCombination> winCombinations) {
        this.winCombinations = winCombinations;
    }
    public StandardSymbol getStandardSymbolConfig(int row, int col) {
        return standardSymbols.stream()
                .filter(p -> p.getRow() == row && p.getColumn() == col)
                .findAny()
                .orElse(standardSymbols.stream().findFirst()
                        .orElseThrow(() -> new RuntimeException("Standard symbols not configured")));
    }
}
