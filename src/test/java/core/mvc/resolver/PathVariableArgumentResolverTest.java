package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.MethodParameter;
import core.mvc.utils.PathPatternMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathVariableArgumentResolverTest {

    private PathVariableArgumentResolver resolver;
    private MethodParameter methodParameter;

    @BeforeEach
    void setup()  {
        Class<?> clazz = PathVariableArgumentResolverTest.class;
        Method[] methods = clazz.getDeclaredMethods();
        Method method = null;
        for (Method m : methods) {
            if (m.getName().equals("for_path_variable_test"))
                method = m;
        }

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        PathPattern pathPattern = PathPatternMatcher.parse(requestMapping.value());

        Parameter param = method.getParameters()[0];
        resolver = new PathVariableArgumentResolver(pathPattern);
        methodParameter = new MethodParameter("id", param);
    }

    @Test
    void supports() {
        assertTrue(resolver.supports(methodParameter));
    }


    @Test
    void get_argument() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        MethodParameter parameter = new MethodParameter("id", int.class);
        int id = (int) resolver.getMethodArgument(parameter, request, response);

        assertEquals(1, id);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    void for_path_variable_test(@PathVariable int id) {
    }
}