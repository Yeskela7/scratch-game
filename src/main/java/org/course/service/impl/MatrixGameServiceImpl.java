package org.course.service.impl;

import java.math.BigDecimal;
import org.course.model.request.Config;
import org.course.model.response.Response;
import org.course.service.MatrixGameService;
import org.course.service.MatrixGenerator;
import org.course.service.RewardCalculator;

public class MatrixGameServiceImpl implements MatrixGameService {

    private final MatrixGenerator matrixGenerator;
    private final RewardCalculator rewardCalculator;

    public MatrixGameServiceImpl(MatrixGenerator matrixGenerator, RewardCalculator rewardCalculator) {
        this.matrixGenerator = matrixGenerator;
        this.rewardCalculator = rewardCalculator;
    }

    @Override
    public Response playMatrixGame(BigDecimal bet, Config config) {
        Response response = new Response();
        String[][] generatedMatrix = matrixGenerator.generateMatrix(config);
        response.setMatrix(generatedMatrix);
        response = rewardCalculator.calculateReward(response, config, bet);
        return response;
    }
}
