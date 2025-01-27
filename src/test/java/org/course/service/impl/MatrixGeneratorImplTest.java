package org.course.service.impl;

import static org.course.mock.MockData.getMockConfigWithBonusSymbols;
import static org.course.mock.MockData.getMockConfigWithoutBonusSymbols;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

import java.util.Random;
import org.course.model.request.Config;
import org.course.service.MatrixGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MatrixGeneratorImplTest {

    @Test
    void generateMatrix() {
        MatrixGenerator matrixGenerator = new MatrixGeneratorImpl(new Random());
        Config config = getMockConfigWithoutBonusSymbols();
        String[][] testMatrix = matrixGenerator.generateMatrix(config);
        String[][] expectedMatrix = {{"A", "B", "C"}};
        assertArrayEquals(expectedMatrix, testMatrix);
    }
    @Test
    void generateMatrixWithBonus() {
        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextDouble()).thenReturn( 0.1);
        MatrixGenerator matrixGenerator = new MatrixGeneratorImpl(mockRandom);
        Config config = getMockConfigWithBonusSymbols();
        String[][] expectedMatrix = {{"x10", "B", "A"}};
        String[][] testMatrix = matrixGenerator.generateMatrix(config);
        assertArrayEquals(expectedMatrix, testMatrix);
    }
}