package core.mvc;

import core.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

@Configuration
class Environment {

    static final String DEFAULT_ENV_RESOURCE = "application.properties";
    static final String ANNOTATION_PACKAGE = "annotation.package";

    private static final Logger logger = LoggerFactory.getLogger(Environment.class);

    private Properties properties;

    private Environment(Properties properties) {
        this.properties = properties;
    }

    static Environment ofDefault() {
        return of(DEFAULT_ENV_RESOURCE);
    }

    static Environment of(String resourceName) {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties(resourceName);
            return new Environment(properties);
        } catch (IOException e) {
            logger.error("resource loader fail : ", resourceName);
            throw new IllegalArgumentException("설정 파일을 읽는데 실패하였습니다. ", e);
        }
    }

    String getProperty(String key) {
        return properties.getProperty(key);
    }
}
