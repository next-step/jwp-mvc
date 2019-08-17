package core.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static core.mvc.Environment.DEFAULT_ENV_RESOURCE;
import static org.assertj.core.api.Assertions.assertThat;

class EnvironmentTest {

    private Environment environment;

    @BeforeEach
    void setUp() {
        environment = Environment.of(DEFAULT_ENV_RESOURCE);
    }

    @DisplayName("설정 파일을 불러온다")
    @Test
    void createProperties() {
        environment = Environment.of(DEFAULT_ENV_RESOURCE);

        assertThat(environment).isNotNull();
    }

    @DisplayName("키의 값을 반환한다")
    @Test
    void getProperty() {
        String key = "annotation.package";

        String value = environment.getProperty(key);

        assertThat(value).isEqualTo("next.controller");
    }

    @DisplayName("키에 할당된 값이 없을 시 null 반환한다")
    @Test
    void getProperty_whenNonKey_thenReturnNull() {
        String value = environment.getProperty("nonKey");

        assertThat(value).isNull();
    }
}