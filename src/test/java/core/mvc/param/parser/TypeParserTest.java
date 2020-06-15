package core.mvc.param.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("타입 파서")
class TypeParserTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("인트 파싱이 제대로 되는지")
    void parseInt(final Class<?> type) {
        Object value = TypeParser.parse(type, "1");

        assertThat(value).isInstanceOf(Integer.class);
        assertThat(value).isEqualTo(1);
    }

    private static Stream<Class<?>> parseInt() {
        return Stream.of(
                int.class,
                Integer.class
        );
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("롱 파싱이 제대로 되는지")
    void parseLong(final Class<?> type) {
        Object value = TypeParser.parse(type, "1");

        assertThat(value).isInstanceOf(Long.class);
        assertThat(value).isEqualTo(1L);
    }

    private static Stream<Class<?>> parseLong() {
        return Stream.of(
                long.class,
                Long.class
        );
    }

    @Test
    @DisplayName("스트링 파싱이 제대로 되는지")
    void parseString() {
        Object value = TypeParser.parse(String.class, "1");

        assertThat(value).isInstanceOf(String.class);
        assertThat(value).isEqualTo("1");
    }
}