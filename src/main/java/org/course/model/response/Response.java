package org.course.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@JsonSerialize
public class Response {
    private String[][] matrix;
    private BigDecimal reward;
    @JsonProperty("applied_winning_combinations")
    private Map<String, List<String>> appliedWinningCombinations;
    @JsonProperty("applied_bonus_symbol")
    private String appliedBonusSymbol;

    public String[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(String[][] matrix) {
        this.matrix = matrix;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public Map<String, List<String>> getAppliedWinningCombinations() {
        return appliedWinningCombinations;
    }

    public void setAppliedWinningCombinations(
            Map<String, List<String>> appliedWinningCombinations) {
        this.appliedWinningCombinations = appliedWinningCombinations;
    }

    @Override
    public String toString() {
        return "Response{" +
                "matrix=" + Arrays.deepToString(matrix) +
                ", reward=" + reward +
                ", appliedWinningCombinations=" + appliedWinningCombinations +
                ", appliedBonusSymbol='" + appliedBonusSymbol + '\'' +
                '}';
    }


    public void setAppliedBonusSymbol(String appliedBonusSymbol) {
        this.appliedBonusSymbol = appliedBonusSymbol;
    }

}
