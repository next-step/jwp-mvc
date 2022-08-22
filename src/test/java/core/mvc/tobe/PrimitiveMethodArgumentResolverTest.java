package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class PrimitiveMethodArgumentResolverTest {

    @DisplayName("type resolve 테스트")
    @Test
    void resolveTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String s = "strType";
        int i = 1;
        long l = 1L;
        double d = 1.0d;

        request.addParameter("strType", s);
        request.addParameter("intType", String.valueOf(i));
        request.addParameter("longType", String.valueOf(l));
        request.addParameter("doubleType", String.valueOf(d));

        Class clazz = MyController.class;
        Method method = getMethod("primitiveMethodArgumentResolverTest", clazz.getDeclaredMethods());

        MethodParameter strType = new MethodParameter(method, 0, null);
        MethodParameter intType = new MethodParameter(method, 1, null);
        MethodParameter longType = new MethodParameter(method, 2, null);
        MethodParameter doubleType = new MethodParameter(method, 3, null);

        PrimitiveMethodArgumentResolver resolver = new PrimitiveMethodArgumentResolver();
        Object param1 = resolver.resolveArgument(strType, request);
        Object param2 = resolver.resolveArgument(intType, request);
        Object param3 = resolver.resolveArgument(longType, request);
        Object param4 = resolver.resolveArgument(doubleType, request);

        assertThat(param1).isEqualTo(s);
        assertThat(param2).isEqualTo(i);
        assertThat(param3).isEqualTo(l);
        assertThat(param4).isEqualTo(d);
    }

    @DisplayName("isSupported 테스트")
    @Test
    void isSupportedTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        int i = 1;

        request.addParameter("intType", String.valueOf(i));

        Class clazz = MyController.class;
        Method method = getMethod("primitiveMethodArgumentResolverTest", clazz.getDeclaredMethods());

        MethodParameter intType = new MethodParameter(method, 1, null);

        PrimitiveMethodArgumentResolver resolver = new PrimitiveMethodArgumentResolver();
        assertTrue(resolver.supportsParameter(intType));
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }
}
