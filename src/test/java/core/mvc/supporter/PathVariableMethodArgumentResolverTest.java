package core.mvc.supporter;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;

class PathVariableMethodArgumentResolverTest {

    @Test
    void resolveArgument() throws Exception {
        // given
        String userId = "tester";
        PathVariableMethodArgumentResolver resolver = new PathVariableMethodArgumentResolver();

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/" + userId);

        Method method = TestController.class.getMethod("test", String.class);
        Parameter parameter = method.getParameters()[0];

        // when
        String actual = (String) resolver.resolveArgument(parameter, method, request, 0);

        // then
        assertThat(actual).isEqualTo(userId);
    }

    @Controller
    public static class TestController {
        @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
        public void test(@PathVariable String id) {
        }
    }
}
