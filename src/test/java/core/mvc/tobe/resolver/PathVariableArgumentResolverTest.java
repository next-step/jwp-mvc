package core.mvc.tobe.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class PathVariableArgumentResolverTest extends MethodUtils {
    private static List<MethodParameter> methodParameters = new ArrayList<>();
    private static MethodParameter invalidMethodParameter;
    private static PathVariableArgumentResolver pathVariableArgumentResolver = new PathVariableArgumentResolver();

    @BeforeAll
    static void setUp() {
        Class clazz = PathVariableArgumentResolverTest.class;
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
    @DisplayName("PathVariable 애노테이션이 붙은 파라미터를 resolve 할 수 있다.")
    void resolveArgument() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/test/wu2ee/1/password/28");

        // when
        List<Object> resolvedArguments = new ArrayList<>();
        for (MethodParameter methodParameter : methodParameters) {
            resolvedArguments.add(pathVariableArgumentResolver.resolveArgument(methodParameter, request, response));
        }

        // then
        assertThat(resolvedArguments).containsExactly("wu2ee", 1, "password", 28);
    }

    @Test
    @DisplayName("PathVariable 애노테이션에 value, name 의 값이 같은 경우 예외가 발생한다.")
    void throwExceptionBothNameAndValueAssign() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/test/wu2ee");
        // when & then
        assertThatThrownBy(() -> pathVariableArgumentResolver.resolveArgument(invalidMethodParameter, request, response))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @RequestMapping("/test/{name}/{id}/{password}/{age}")
    public void testMethod(@PathVariable String name,
                           @PathVariable int id,
                           @PathVariable(value = "password") String uniquePassword,
                           @PathVariable(name = "age") int currentAge) {
    }

    @RequestMapping("/test/{invalid}")
    public void invalidMethod(@PathVariable(value = "invalid", name = "invalid") String name) {
    }
}
