package core.mvc.param.extractor;

import core.annotation.web.PathVariable;
import core.mvc.param.Parameter;
import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @Test
    @DisplayName("사용자 정의 클래스에 대한 테스트")
    void extractComplexValue() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Parameter parameter = new Parameter("user", TestUser.class, null);
        request.setParameter("userId", "nokchax");
        request.setParameter("password", "1234");
        request.setParameter("age", "30");

        Object extract = ValueExtractors.extractValue(parameter, request);

        assertThat(extract).isNotNull();
        assertThat(extract).isInstanceOf(TestUser.class);
        assertThat(((TestUser) extract).getUserId()).isEqualTo("nokchax");
        assertThat(((TestUser) extract).getPassword()).isEqualTo("1234");
        assertThat(((TestUser) extract).getAge()).isEqualTo(30);
    }

    @Test
    @DisplayName("어노테이션이 붙은 클래스에 대한 테스트")
    void extractAnnotationAttachedParameter() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("user", "nokchax");
        Parameter parameter = new Parameter("user", String.class, new PathVariable("123", "123", false));

        Object value = ValueExtractors.extractValue(parameter, request);

        assertThat(value).isInstanceOf(String.class);
        assertThat(value).isEqualTo("nokchax");
    }
}