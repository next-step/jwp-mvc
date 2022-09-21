package core.mvc.resolver;

import core.mvc.tobe.TestUserController;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HttpServletRequestArgumentResolverTest {

    private final HttpServletRequestArgumentResolver resolver = new HttpServletRequestArgumentResolver();

    private Method getMethod(String methodName) throws NoSuchMethodException {
        Class<TestUserController> clazz = TestUserController.class;
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName))
                .findFirst()
                .orElseThrow(NoSuchMethodException::new);
    }

    @Test
    void supportParameter() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Method method = getMethod("httpServletRequest");
        Parameter[] parameters = method.getParameters();
        MethodParameter methodParameter = new MethodParameter(method, parameters[0]);
        boolean actual = resolver.supportsParameter(methodParameter);

        assertEquals(true, actual);
    }


    @Test
    void resolveArgument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Method method = getMethod("httpServletRequest");
        Parameter[] parameters = method.getParameters();
        MethodParameter methodParameter = new MethodParameter(method, parameters[0]);
        Object actual = resolver.resolveArgument(methodParameter, request);

        assertEquals(request, actual);
    }
}
