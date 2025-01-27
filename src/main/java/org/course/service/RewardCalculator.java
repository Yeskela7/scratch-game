package org.course.service;

import java.math.BigDecimal;
import org.course.model.request.Config;
import org.course.model.response.Response;

public interface RewardCalculator {
    Response calculateReward(Response response, Config config, BigDecimal bet);

}
