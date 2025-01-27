package org.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;
import org.course.model.request.Config;
import org.course.model.response.Response;
import org.course.service.MatrixGameService;
import org.course.service.MatrixGenerator;
import org.course.service.RewardCalculator;
import org.course.service.impl.MatrixGameServiceImpl;
import org.course.service.impl.MatrixGeneratorImpl;
import org.course.service.impl.RewardCalculatorImpl;
import org.course.utils.ConfigParser;

public class Main {

    public static void main(String[] args) {
        try {
            Arguments arguments = parseArguments(args);
            Config config = ConfigParser.loadConfig(arguments.getConfigPath());
            MatrixGenerator matrixGenerator = new MatrixGeneratorImpl(new Random());
            RewardCalculator rewardCalculator = new RewardCalculatorImpl();
            MatrixGameService matrixGameService = new MatrixGameServiceImpl(matrixGenerator, rewardCalculator);

            Response result = matrixGameService.playMatrixGame(arguments.getBettingAmount(), config);
            System.out.println(result); // for console
            writeResultToJsonFile(result, arguments.getConfigPath().replaceAll("config.json","result.json"));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error: Unable to read config file or write output file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Arguments parseArguments(String[] args) {
        String configPath = null;
        BigDecimal bettingAmount = BigDecimal.ZERO;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--config":
                    if (i + 1 < args.length) {
                        configPath = args[++i];
                    } else {
                        throw new IllegalArgumentException("Missing value for --config");
                    }
                    break;
                case "--betting-amount":
                    if (i + 1 < args.length) {
                        try {
                            bettingAmount = BigDecimal.valueOf(Integer.parseInt(args[++i]));
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Invalid value for --betting-amount. Must be an integer.");
                        }
                    } else {
                        throw new IllegalArgumentException("Missing value for --betting-amount");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown argument: " + args[i]);
            }
        }

        if (configPath == null) {
            throw new IllegalArgumentException("--config is required.");
        }

        return new Arguments(configPath, bettingAmount);
    }

    private static void writeResultToJsonFile(Response result, String outputFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFilePath), result);
        System.out.println("Result written to " + outputFilePath);
    }

    private static class Arguments {
        private final String configPath;
        private final BigDecimal bettingAmount;

        public Arguments(String configPath, BigDecimal bettingAmount) {
            this.configPath = configPath;
            this.bettingAmount = bettingAmount;
        }

        public String getConfigPath() {
            return configPath;
        }

        public BigDecimal getBettingAmount() {
            return bettingAmount;
        }
    }
}