package org.course.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import org.course.model.request.Config;

public class ConfigParser {
    public static Config loadConfig(String configPath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(configPath), Config.class);
    }
}