package core.mvc.resolver;

import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;

import static core.mvc.resolver.ResolverUtils.generateMethodParameters;
import static core.mvc.resolver.ResolverUtils.getMethod;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelArgumentResolverTest {

    private final ModelAttributeArgumentResolver resolver = new ModelAttributeArgumentResolver();

    @DisplayName("ModelAttributeArgumentResolver는 JavaBean 타입을 지원한다.")
    @Test
    void supportParameter() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "1");
        request.addParameter("password", "1234");
        request.addParameter("age", "20");

        Method method = getMethod("create_javabean");
        generateMethodParameters(method)
                .forEach(methodParameter -> assertTrue(resolver.supportsParameter(methodParameter)));
    }

    @DisplayName("ModelAttributeArgumentResolver는 JavaBean 타입을 resolve한다.")
    @Test
    void resolveArgument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "1");
        request.addParameter("password", "1234");
        request.addParameter("age", "20");

        Method method = getMethod("create_javabean");
        MethodParameter parameter = generateMethodParameters(method).get(0);
        TestUser actual = (TestUser) resolver.resolveArgument(parameter, request);
        TestUser expected = new TestUser("1", "1234", 20);

        assertEquals(expected, actual);
    }
}
