package core.mvc.support;

import core.annotation.web.PathVariable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class MethodParameterTest {

    @DisplayName("어노테이션이 존재하고 잘 가져오는지 테스트")
    @Test
    void test_get_annotation() throws Exception {
        final Method method = MethodParameterTestObject.class.getMethod("test", long.class);
        final MethodSignature methodSignature = new MethodSignature(method);
        final MethodParameter parameter = methodSignature.getMethodParameters().get(0);
        assertThat(parameter.getAnnotation(PathVariable.class)).isInstanceOf(PathVariable.class);
    }

    public static class MethodParameterTestObject {
        public void test(@PathVariable long id) {
            // no-op
        }
    }
}