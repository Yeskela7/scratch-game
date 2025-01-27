package org.course.service.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.course.mock.MockData;
import org.course.model.request.Config;
import org.course.model.response.Response;
import org.course.service.MatrixGameService;
import org.course.service.MatrixGenerator;
import org.course.service.RewardCalculator;
import org.junit.jupiter.api.Test;

class MatrixGameServiceImplTest {

    @Test
    void testPlayMatrixGame() {

        MatrixGenerator matrixGeneratorMock = mock(MatrixGenerator.class);
        RewardCalculator rewardCalculator = new RewardCalculatorImpl();
        MatrixGameService matrixGameService = new MatrixGameServiceImpl(matrixGeneratorMock, rewardCalculator);

        BigDecimal bet = BigDecimal.valueOf(100);
        Config config = MockData.createMockConfig();

        String[][] mockMatrix = {
        {"A", "A", "B"},
        {"A", "+1000", "B"},
        {"A", "A", "B"}
        };

        when(matrixGeneratorMock.generateMatrix(config)).thenReturn(mockMatrix);

        Response response = matrixGameService.playMatrixGame(bet, config);

        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getMatrix(), "Generated matrix should not be null");
        assertArrayEquals(mockMatrix, response.getMatrix(), "Generated matrix should match the mock matrix");
        assertEquals(0, BigDecimal.valueOf(6600).compareTo(response.getReward()));

        verify(matrixGeneratorMock, times(1)).generateMatrix(config);
    }
}
