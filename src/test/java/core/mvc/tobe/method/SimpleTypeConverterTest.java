package core.mvc.tobe.method;

import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("TypeConverter 테스트")
class SimpleTypeConverterTest {

    @DisplayName("Converter 지원 여부")
    @Test
    void support() {
        SimpleTypeConverter converter = SimpleTypeConverter.getTypeConverter();

        assertAll(
                () -> assertThat(converter.isSupports(Integer.class)).isTrue(),
                () -> assertThat(converter.isSupports(TestUser.class)).isFalse()
        );
    }

    @DisplayName("정상 변환 확인")
    @Test
    void convert() {
        assertThat(SimpleTypeConverter.getTypeConverter().convert("1", Integer.TYPE))
                .isEqualTo(1);
    }
}
