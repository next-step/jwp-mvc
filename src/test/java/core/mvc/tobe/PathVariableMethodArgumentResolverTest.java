package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class PathVariableMethodArgumentResolverTest {
    @DisplayName("PathVariable Annotation resolve 테스트")
    @Test
    void resolveTest(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        String s = "name";
        long l = 1L;

        request.addParameter("id", String.valueOf(l));
        request.addParameter("name", s);

        Class clazz = MyController.class;
        Method method = getMethod("pathVariableMethodArgumentResolverTest", clazz.getDeclaredMethods());

        MethodParameter longType = new MethodParameter(method, 0, method.getAnnotations());
        MethodParameter strType = new MethodParameter(method, 1, method.getAnnotations());

        PrimitiveMethodArgumentResolver resolver = new PrimitiveMethodArgumentResolver();
        Object param1 = resolver.resolveArgument(longType, request);
        Object param2 = resolver.resolveArgument(strType, request);

        assertThat(param1).isEqualTo(l);
        assertThat(param2).isEqualTo(s);
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }
}