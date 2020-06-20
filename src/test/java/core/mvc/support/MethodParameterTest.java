package core.mvc.support;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class MethodParameterTest {

    private MethodParameter parameter;

    @BeforeEach
    void setUp() throws Exception {
        final Method method = MethodParameterTestObject.class.getMethod("test", long.class);
        final MethodSignature methodSignature = new MethodSignature(method);
        parameter = methodSignature.getMethodParameters().get(0);
    }

    @DisplayName("어노테이션이 존재하고 잘 가져오는지 테스트")
    @Test
    void test_get_parameter_annotation() throws Exception {
        assertThat(parameter.getParameterAnnotation(PathVariable.class)).isInstanceOf(PathVariable.class);
    }

    @DisplayName("메소드에 붙은 어노테이션을 갖고온다..!")
    @Test
    void test_get_method_annotation() throws Exception {
        assertThat(parameter.getMethodAnnotation(RequestMapping.class)).isInstanceOf(RequestMapping.class);
    }

    public static class MethodParameterTestObject {
        @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
        public void test(@PathVariable long id) {
            // no-op
        }
    }
}