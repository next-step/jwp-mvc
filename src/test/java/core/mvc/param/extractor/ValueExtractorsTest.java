package core.mvc.param.extractor;

import core.mvc.param.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("모든 종류의 파라미터 값 추출 클래스를 모아서 해당하는 정보에 대한 파라미터를 리턴해 주는 클래스")
class ValueExtractorsTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("일반 값 추출 테스트")
    void extractSimpleValue(final Class<?> clazz, final Object expected) {
        Parameter parameter = new Parameter("id", clazz, null);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("id", "1");
        Object value = ValueExtractors.extractValue(parameter, request);

        assertThat(value).isEqualTo(expected);
    }

    private static Stream<Arguments> extractSimpleValue() {
        return Stream.of(
                Arguments.of(int.class, 1),
                Arguments.of(Integer.class, 1),
                Arguments.of(long.class, 1L),
                Arguments.of(Long.class, 1L),
                Arguments.of(String.class, "1")
        );
    }
}