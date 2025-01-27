package org.course.service;

import org.course.model.request.Config;

public interface MatrixGenerator {
    String[][] generateMatrix(Config config);
}
