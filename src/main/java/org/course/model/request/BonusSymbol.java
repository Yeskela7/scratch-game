package org.course.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class BonusSymbol {
    @JsonProperty("symbols")
    private Map<String, Integer> bonusSymbols;

    public Map<String, Integer> getBonusSymbols() {
        return bonusSymbols;
    }

    public void setBonusSymbols(Map<String, Integer> bonusSymbols) {
        this.bonusSymbols = bonusSymbols;
    }

    public BonusSymbol(Map<String, Integer> bonusSymbols) {
        this.bonusSymbols = bonusSymbols;
    }

    public BonusSymbol() {
    }
}
