package core.mvc.tobe.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @DisplayName("빈 값이 빈 값인지 확인 시 true를 반환한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "     "})
    @ParameterizedTest
    void isBlankTrue(final String value) {
        assertThat(StringUtils.isBlank(value)).isTrue();
    }

    @DisplayName("빈 값이 아닌 값을 빈 값인지 확인 시 false를 반환한다.")
    @ValueSource(strings = {"1", " 1", " 1 "})
    @ParameterizedTest
    void isBlankFalse(final String value) {
        assertThat(StringUtils.isBlank(value)).isFalse();
    }

    @DisplayName("빈 값이 아닌 값을 빈 값인지 확인 시 true를 반환한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "     "})
    @ParameterizedTest
    void isNotBlankTrue(final String value) {
        assertThat(StringUtils.isBlank(value)).isTrue();
    }

    @DisplayName("빈 값이 아닌 값을 빈 값이 아닌지 확인 시 false를 반환한다.")
    @ValueSource(strings = {"1", " 1", " 1 "})
    @ParameterizedTest
    void isNotBlankFalse(final String value) {
        assertThat(StringUtils.isBlank(value)).isFalse();
    }
}