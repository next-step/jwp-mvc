package core.mvc;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class Environment {

    public static String RESOURCE_NAME = "config.properties";

    public Properties config;

    public Environment() {
        try {
            config = PropertiesLoaderUtils.loadAllProperties(RESOURCE_NAME);
        } catch (IOException e) {
            throw new IllegalArgumentException(RESOURCE_NAME + " config invalid");
        }
    }

    public String getProperty(String key) {
        return config.getProperty(key);
    }

    public void setConfig(Properties config) {
        this.config = config;
    }
}
