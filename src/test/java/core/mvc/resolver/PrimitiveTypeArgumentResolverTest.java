package core.mvc.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.util.List;

import static core.mvc.resolver.ResolverUtils.generateMethodParameters;
import static core.mvc.resolver.ResolverUtils.getMethod;
import static org.junit.jupiter.api.Assertions.*;

class PrimitiveTypeArgumentResolverTest {

    private final PrimitiveTypeArgumentResolver resolver = new PrimitiveTypeArgumentResolver();

    @DisplayName("PrimitiveTypeArgumentResolver는 String 타입을 지원한다.")
    @Test
    void supportParameterWithString() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "jiwon");
        request.addParameter("password", "1234");

        Method method = getMethod("create_string");
        List<MethodParameter> methodParameters = generateMethodParameters(method);

        methodParameters.forEach(methodParameter -> assertEquals(true, resolver.supportsParameter(methodParameter)));
    }

    @DisplayName("PrimitiveTypeArgumentResolver는 int, long 타입을 지원한다.")
    @Test
    void supportParameterWithIntAndLong() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", "1");
        request.addParameter("age", "20");

        Method method = getMethod("create_int_long");
        List<MethodParameter> methodParameters = generateMethodParameters(method);

        methodParameters.forEach(methodParameter -> assertEquals(true, resolver.supportsParameter(methodParameter)));
    }

    @DisplayName("PrimitiveTypeArgumentResolver는 String 타입을 resolve하여 반환한다.")
    @Test
    void resolveArgumentWithString() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "jiwon");
        request.addParameter("password", "1234");

        Method method = getMethod("create_string");
        List<MethodParameter> methodParameters = generateMethodParameters(method);

        assertEquals("jiwon", resolver.resolveArgument(methodParameters.get(0), request));
        assertEquals("1234", resolver.resolveArgument(methodParameters.get(1), request));
    }

    @DisplayName("PrimitiveTypeArgumentResolver는 int, long 타입을 resolve하여 반환한다.")
    @Test
    void resolveArgumentWithIntAndLong() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", "1");
        request.addParameter("age", "20");

        Method method = getMethod("create_int_long");
        List<MethodParameter> methodParameters = generateMethodParameters(method);

        assertEquals(1L, resolver.resolveArgument(methodParameters.get(0), request));
        assertEquals(20, resolver.resolveArgument(methodParameters.get(1), request));
    }
}
