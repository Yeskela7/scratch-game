package org.course.service.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.course.model.request.Config;
import org.course.service.MatrixGenerator;

public class MatrixGeneratorImpl implements MatrixGenerator {

    private final Random random;

    public MatrixGeneratorImpl(Random random) {
        this.random = random;
    }

    @Override
    public String[][] generateMatrix(Config config) {
        String[][] matrix = new String[config.getRows()][config.getColumns()];
        for (int i = 0; i < config.getRows(); i++) {
            for (int j = 0; j < config.getColumns(); j++) {
                Map<String, Integer> symbols = getCellProbabilities(config, i, j);
                matrix[i][j] = generateSymbol(symbols);
            }
        }
        Map<String, Integer> allBonusSymbols = getAllBonusSymbols(config);
        addBonusSymbol(matrix, allBonusSymbols);
        return matrix;
    }


    private void addBonusSymbol(String[][] matrix,
                                  Map<String, Integer> allBonusSymbols) {
        if (!allBonusSymbols.isEmpty() && random.nextDouble() < 0.5) {
            int row = random.nextInt(matrix.length);
            int col = random.nextInt(matrix[0].length);
            matrix[row][col] = generateSymbol(allBonusSymbols);
        }
    }


    private Map<String, Integer> getAllBonusSymbols(Config config) {
        if (config.getProbabilities().getBonusSymbols() == null
                || config.getProbabilities().getBonusSymbols().getBonusSymbols().isEmpty()) {
            return Collections.emptyMap();
        }
        return config.getProbabilities().getBonusSymbols()
                .getBonusSymbols().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    private static Map<String, Integer> getCellProbabilities(Config config, int i, int j) {
        return config.getProbabilities().getStandardSymbolConfig(i, j).getSymbols().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    private String generateSymbol(Map<String, Integer> symbols) {
        int totalProbability = symbols.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalProbability);

        int cumulativeProbability = 0;
        for (Map.Entry<String, Integer> entry : symbols.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue < cumulativeProbability) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Symbol generation failed.");
    }
}
