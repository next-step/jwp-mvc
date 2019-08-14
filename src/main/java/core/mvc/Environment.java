package core.mvc;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class Environment {

    public static String RESOURCE_NAME = "config.properties";

    public Properties config;

    public Environment() {
        this(RESOURCE_NAME);
    }

    public Environment(String resourceName) {
        try {
            config = PropertiesLoaderUtils.loadAllProperties(resourceName);
        } catch (IOException e) {
            throw new IllegalArgumentException(resourceName + " config invalid");
        }
    }

    public String getProperty(String key) {
        return config.getProperty(key);
    }

    public void setConfig(Properties config) {
        this.config = config;
    }
}
