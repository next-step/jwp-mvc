package core.mvc.tobe.method;

import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("간단 타입 변환기")
class SimpleTypeConverterTest {

    @Test
    @DisplayName("싱글톤 객체")
    void instance() {
        assertThatNoException()
                .isThrownBy(SimpleTypeConverter::instance);
    }

    @Test
    @DisplayName("신규 생성 불가")
    void newInstance_thrownAssertionError() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> ReflectionUtils.newInstance(SimpleTypeConverter.class));
    }

    @Test
    @DisplayName("지원 여부")
    void isSupports() {
        //given
        SimpleTypeConverter instance = SimpleTypeConverter.instance();
        //when, then
        assertAll(
                () -> assertThat(instance.isSupports(String.class)).isTrue(),
                () -> assertThat(instance.isSupports(TestUser.class)).isFalse()
        );
    }

    @Test
    @DisplayName("변환")
    void convert() {
        assertThat(SimpleTypeConverter.instance().convert("1", Long.TYPE))
                .isEqualTo(1L);
    }
}
