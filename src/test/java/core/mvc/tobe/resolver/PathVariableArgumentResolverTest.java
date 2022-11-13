package core.mvc.tobe.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.MethodParameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PathVariableArgumentResolverTest {

    private static final Logger logger = LoggerFactory.getLogger(PathVariableArgumentResolverTest.class);

    private PathVariableArgumentResolver resolver = new PathVariableArgumentResolver();

    @DisplayName("유효한 경로로 요청하면 경로에서 인자값을 추출하여 주입한다.")
    @Test
    void injectPathVariable() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setRequestURI("/users/ljh0326");

        Method method = getMethod("pathVariable", MyController.class.getDeclaredMethods());
        MethodParameter methodParameter = new MethodParameter(method, getParameter(method));

        Object value = resolver.resolveArgument(methodParameter, request, response, "userId");

        assertThat(value).isEqualTo("ljh0326");
    }

    private static Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }

    private static Parameter getParameter(Method method) {
        Parameter[] parameters = method.getParameters();
        return parameters[0];
    }

        class MyController {
        @RequestMapping("/users/{userId}")
        public void pathVariable(@PathVariable(value = "userId") String userId) {
            logger.info("userId:{}", userId);
        }

        @RequestMapping("/users/{userId}")
        public void pathVariableWithRequireFalse(@PathVariable(required = false) String userId) {
            logger.info("userId:{}", userId);
        }
    }
}