package core.mvc.tobe.resolver;

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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleTypeArgumentResolverTest extends MethodUtils {
    private static final List<MethodParameter> methodParameters = new ArrayList<>();
    private static SimpleTypeArgumentResolver simpleTypeArgumentResolver = new SimpleTypeArgumentResolver();

    @BeforeAll
    static void setUp() {
        Class clazz = SimpleTypeArgumentResolverTest.class;
        Method method = getMethod("testMethod", clazz.getDeclaredMethods());
        Parameter[] parameters = method.getParameters();

        int parameterCount = parameters.length;

        for (int idx = 0; idx < parameterCount; idx++) {
            methodParameters.add(new MethodParameter(method, parameters[idx], idx));
        }
    }

    @Test
    @DisplayName("Simple Type 파라미터를 resolve 할 수 있다.")
    void resolveArgument() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String string = "string";
        String primitiveInt = "1";
        String wrapperInt = "2";
        String primitiveLong = "5000000000";
        String wrapperLong = "6000000000";
        String primitiveBool = "true";
        String wrapperBool = "false";
        String primitiveDouble = "1.0";
        String wrapperDouble = "2.1";
        request.addParameter("string", string);
        request.addParameter("primitiveInt", primitiveInt);
        request.addParameter("wrapperInt", wrapperInt);
        request.addParameter("primitiveLong", primitiveLong);
        request.addParameter("wrapperLong", wrapperLong);
        request.addParameter("primitiveBool", primitiveBool);
        request.addParameter("wrapperBool", wrapperBool);
        request.addParameter("primitiveDouble", primitiveDouble);
        request.addParameter("wrapperDouble", wrapperDouble);

        // when
        List<Object> resolvedArguments = methodParameters.stream()
                .map(methodParameter -> simpleTypeArgumentResolver.resolveArgument(methodParameter, request, response))
                .collect(Collectors.toList());

        // then
        assertThat(resolvedArguments).containsExactly("string", 1, 2, 5000000000L, 6000000000L, true, false, 1.0, 2.1);
    }

    public void testMethod(String string,
                           int primitiveInt,
                           Integer wrapperInt,
                           long primitiveLong,
                           Long wrapperLong,
                           boolean primitiveBool,
                           Boolean wrapperBool,
                           double primitiveDouble,
                           double wrapperDouble) {
    }
}
