package core.mvc.resolver;

import core.mvc.MethodParameter;
import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ModelArgumentResolverTest {

    private ModelArgumentResolver modelArgumentResolver;

    @BeforeEach
    void setUp() {
        modelArgumentResolver = new ModelArgumentResolver();
    }

    @DisplayName("지원하는 메서드 파라미터")
    @Test
    void 지원하는메서드파라미터() {
        MethodParameter methodParameter = new MethodParameter("testParam", TestUser.class, null, null);
        assertThat(modelArgumentResolver.supports(methodParameter)).isTrue();
    }

    @DisplayName("primitive type 또는 primitive wrapper type은 지원하지 않는다")
    @ParameterizedTest
    @MethodSource("failTestCase")
    void 지원하지않는메서드파라미터(MethodParameter methodParameter) {
        assertThat(modelArgumentResolver.supports(methodParameter)).isFalse();
    }

    private static Stream<Arguments> failTestCase() {
        return Stream.of(
                Arguments.of(new MethodParameter("testParam", Integer.class, null, null)),
                Arguments.of(new MethodParameter("testParam", int.class, null, null)),
                Arguments.of(new MethodParameter("testParam", Long.class, null, null)),
                Arguments.of(new MethodParameter("testParam", long.class, null, null)),
                Arguments.of(new MethodParameter("testParam", Boolean.class, null, null)),
                Arguments.of(new MethodParameter("testParam", boolean.class, null, null))
        );
    }

    @DisplayName("메서드에 지정한 클래스로 받는지 검증")
    @Test
    void convertModel() {
        MethodParameter methodParameter = new MethodParameter("testParam", TestUser.class, null, null);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("userId", "testUserId");
        request.setParameter("password", "mypassword");
        request.setParameter("age", "123");
        Object object = modelArgumentResolver.resolveArgument(methodParameter, request, new MockHttpServletResponse());

        assertThat(object).isInstanceOf(TestUser.class);

        assertThat(((TestUser) object).getUserId()).isEqualTo("testUserId");
        assertThat(((TestUser) object).getPassword()).isEqualTo("mypassword");
        assertThat(((TestUser) object).getAge()).isEqualTo(123);
    }
}