package core.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentTest {

    @DisplayName("load config file to environment instance")
    @Test
    void loadEnvironment() {
        Environment.RESOURCE_NAME = "config-test.properties";
        Environment environment = new Environment();
        assertThat(environment.getProperty("name")).isEqualTo("jun");
    }

}
