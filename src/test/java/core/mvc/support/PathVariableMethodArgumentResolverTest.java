package core.mvc.support;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.support.resolvers.HandlerMethodArgumentResolver;
import core.mvc.support.resolvers.PathVariableMethodArgumentResolver;
import core.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathVariableMethodArgumentResolverTest {

    private static final HandlerMethodArgumentResolver resolver = new PathVariableMethodArgumentResolver();

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest("GET", "/users/10");
    }

    @DisplayName("PathVariable 값을 잘 가져오는지 테스트")
    @Test
    void test_path_variable_value() throws Exception {
        final Method method = DummyController.class.getDeclaredMethod("test1", long.class);
        final MethodSignature methodSignature = new MethodSignature(method);
        final MethodParameter parameter = methodSignature.getMethodParameters().get(0);
        assertTrue(resolver.supportsParameter(parameter));
        final Object resolvedData = resolver.resolveArgument(parameter, request);
        assertThat(resolvedData).isEqualTo(10L);
        assertThat(resolvedData).isInstanceOf(Long.class);
    }

    @Controller
    public static class DummyController {
        @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
        public ModelAndView test1(@PathVariable long id) {
            return null;
        }
    }

}