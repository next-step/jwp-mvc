package core.mvc.tobe.resolver;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestParam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestParamArgumentResolverTest extends MethodUtils {
    private static List<MethodParameter> methodParameters = new ArrayList<>();
    private static MethodParameter invalidMethodParameter;
    private static RequestParamArgumentResolver requestParamArgumentResolver = new RequestParamArgumentResolver();

    @BeforeAll
    static void setUp() {
        Class clazz = RequestParamArgumentResolverTest.class;
        Method testMethod = getMethod("testMethod", clazz.getDeclaredMethods());
        Method invalidMethod = getMethod("invalidMethod", clazz.getDeclaredMethods());
        Parameter[] parameters = testMethod.getParameters();
        int parameterCount = parameters.length;

        for (int idx = 0; idx < parameterCount; idx++) {
            methodParameters.add(new MethodParameter(testMethod, parameters[idx], idx));
        }
        invalidMethodParameter = new MethodParameter(invalidMethod, invalidMethod.getParameters()[0], 0);
    }

    @Test
    @DisplayName("RequestParam 애노테이션이 붙은 파라미터를 resolve 할 수 있다.")
    void resolveArgument() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("name", "wu2ee");
        request.addParameter("id", "1");
        request.addParameter("password", "password");
        request.addParameter("age", "28");

        // when
        List<Object> resolvedArguments = methodParameters.stream()
                .map(methodParameter -> requestParamArgumentResolver.resolveArgument(methodParameter, request, response))
                .collect(Collectors.toList());

        // then
        assertThat(resolvedArguments).containsExactly("wu2ee", 1, "password", 28);
    }

    @Test
    @DisplayName("RequestParam 애노테이션에 value, name 이 같이 할당된 경우 예외가 발생한다.")
    void throwExceptionBothNameAndValueAssign() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("invalid", "wu2ee");
        // when & then
        assertThatThrownBy(() -> requestParamArgumentResolver.resolveArgument(invalidMethodParameter, request, response))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @RequestMapping
    public void testMethod(@RequestParam String name,
                           @RequestParam int id,
                           @RequestParam(value = "password") String uniquePassword,
                           @RequestParam(name = "age") int currentAge) {
    }

    @RequestMapping
    public void invalidMethod(@RequestParam(value = "invalid", name = "invalid") String name) {
    }
}
