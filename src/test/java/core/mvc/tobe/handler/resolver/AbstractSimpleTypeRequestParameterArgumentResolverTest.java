package core.mvc.tobe.handler.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AbstractSimpleTypeRequestParameterArgumentResolverTest {
    private static final NamedParameter STRING_TYPE_PARAMETER;

    private final AbstractSimpleTypeRequestParameterArgumentResolver argumentResolver = new AbstractSimpleTypeRequestParameterArgumentResolver() {
        @Override
        Object resolveInternal(String value) {
            return value;
        }

        @Override
        public boolean support(NamedParameter parameter) {
            return true;
        }
    };

    static {
        Method testClassMethod = TestClass.class.getDeclaredMethods()[0];
        Parameter[] parameters = testClassMethod.getParameters();

        STRING_TYPE_PARAMETER = new NamedParameter(parameters[0], "name");
    }

    @DisplayName("메서드 인자명과 일치하는 requestParameter 값이 존재하는 경우, resolveInternal 메서드 호출 값을 반환")
    @Test
    void resolve() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("name", "jordy");

        Object actual = argumentResolver.resolve(STRING_TYPE_PARAMETER, request, new MockHttpServletResponse());
        assertThat(actual).isEqualTo("jordy");
    }

    @DisplayName("메서드 인자명과 일치하는 requestParameter 값이 존재하지 않는 경우, 예외 발생")
    @Test
    void resolve_fail() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThatThrownBy(() -> argumentResolver.resolve(STRING_TYPE_PARAMETER, request, new MockHttpServletResponse()))
                .isInstanceOf(ArgumentResolveFailException.class)
                .hasMessage("requestParameter - [name] 값이 null입니다.");
    }

    private static class TestClass {
        public void test(String name) {

        }
    }
}