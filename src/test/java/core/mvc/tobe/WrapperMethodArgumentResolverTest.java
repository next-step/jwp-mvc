package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class WrapperMethodArgumentResolverTest {
    @DisplayName("Wrapper class resolve 테스트")
    @Test
    void resolveTest(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        String s = "strType";
        Integer i = 1;
        Long l = 1L;
        Double d = 1.0d;

        request.addParameter("strType", s);
        request.addParameter("intType", String.valueOf(i));
        request.addParameter("longType", String.valueOf(l));
        request.addParameter("doubleType", String.valueOf(d));

        Class clazz = MyController.class;
        Method method = getMethod("wrapperMethodArgumentResolverTest", clazz.getDeclaredMethods());

        MethodParameter strType = new MethodParameter(method, 0, null);
        MethodParameter intType = new MethodParameter(method, 1, null);
        MethodParameter longType = new MethodParameter(method, 2, null);
        MethodParameter doubleType = new MethodParameter(method, 3, null);

        WrapperMethodArgumentResolver resolver = new WrapperMethodArgumentResolver();
        Object param1 = resolver.resolveArgument(strType, request);
        Object param2 = resolver.resolveArgument(intType, request);
        Object param3 = resolver.resolveArgument(longType, request);
        Object param4 = resolver.resolveArgument(doubleType, request);

        assertThat(param1).isEqualTo(s);
        assertThat(param1.getClass()).isEqualTo(String.class);

        assertThat(param2).isEqualTo(i);
        assertThat(param2.getClass()).isEqualTo(Integer.class);

        assertThat(param3).isEqualTo(l);
        assertThat(param3.getClass()).isEqualTo(Long.class);

        assertThat(param4).isEqualTo(d);
        assertThat(param4.getClass()).isEqualTo(Double.class);
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }
}