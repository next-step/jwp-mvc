package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.MethodParameter;
import core.mvc.exception.NoSuchArgumentResolverException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathVariableArgumentResolverTest {
    private static final Logger logger = LoggerFactory.getLogger(PathVariableArgumentResolverTest.class);

    private PathVariableArgumentResolver resolver = new PathVariableArgumentResolver();

    @DisplayName("유효한 경로로 요청하면 경로에서 인자값을 추출하여 주입한다.")
    @Test
    void injectPathVariable() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resq = new MockHttpServletResponse();

        req.setRequestURI("/users/catsbi");
        MethodParameter mp = MethodParameter.of(getMethod("pathVariable", MyController.class.getDeclaredMethods()), 0);

        Object value = resolver.resolve(req, resq, "userId", mp);

        assertThat(value).isEqualTo("catsbi");
    }

    @DisplayName("유효하지 않은 경로로 요청하면 예외를 던진다.")
    @Test
    void injectPathVariableWithInvalidPath() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resq = new MockHttpServletResponse();

        req.setRequestURI("/users");
        MethodParameter mp = MethodParameter.of(getMethod("pathVariable", MyController.class.getDeclaredMethods()), 0);

        assertThatThrownBy(()-> resolver.resolve(req, resq, "userId", mp))
                .isInstanceOf(NoSuchArgumentResolverException.class);
    }

    @DisplayName("유효하지 않은 경로로 요청해도 require 속성이 false면 null을 반환한다.")
    @Test
    void injectPathVariableWithInvalidPathAndRequireFalse() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resq = new MockHttpServletResponse();

        req.setRequestURI("/users");
        MethodParameter mp = MethodParameter.of(getMethod("pathVariableWithRequireFalse", MyController.class.getDeclaredMethods()), 0);

        Object value = resolver.resolve(req, resq, "userId", mp);

        assertThat(value).isNull();
    }


    private static Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }

    class MyController {
        @RequestMapping("/users/{userId}")
        public void pathVariable(@PathVariable String userId) {
            logger.info("userId:{}", userId);
        }

        @RequestMapping("/users/{userId}")
        public void pathVariableWithRequireFalse(@PathVariable(required = false) String userId) {
            logger.info("userId:{}", userId);
        }
    }
}
