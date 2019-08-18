package core.mvc.mapping;

import core.mvc.ModelAndView;
import core.mvc.resolver.HandlerMethodArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerMethodArgumentResolverTest {
    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodArgumentResolverTest.class);

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final Class testUserControllerClass = TestUserController.class;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void string() throws Exception {
        String userId = "javajigi";
        String password = "password";
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        Class clazz = TestUserController.class;
        Method method = getMethod("create_string", clazz.getDeclaredMethods());
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            logger.debug("parameter : {}", parameterName);
            values[i] = request.getParameter(parameterName);
        }

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("userId")).isEqualTo(userId);
        assertThat(mav.getObject("password")).isEqualTo(password);
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }

    @DisplayName("파라미터가 인스턴스 일때")
    @Test
    void resolve_instance() throws Exception { ;
        String userId = "javajigi";
        int age = 999;
        request.addParameter("userId", userId);
        request.addParameter("age", String.valueOf(age));

        Method method = getMethod("create_javabean", testUserControllerClass.getDeclaredMethods());

        Object[] results = HandlerMethodArgumentResolver.resolve(method, request, response);

        assertThat(results).hasSize(1);
        assertThat(results[0]).isInstanceOf(TestUser.class);

        TestUser testUser = (TestUser) results[0];
        assertThat(testUser.getUserId()).isEqualTo(userId);
        assertThat(testUser.getAge()).isEqualTo(age);
        assertThat(testUser.getPassword()).isNull();
    }

    @DisplayName("@PathVariable 이 붙은 파라미터")
    @Test
    void resolve_path_variable() {
        long id = 53425435L;
        String uri = "/users/" + id;
        request.setRequestURI(uri);

        Method method = getMethod("show_pathvariable", testUserControllerClass.getDeclaredMethods());

        Object[] results = HandlerMethodArgumentResolver.resolve(method, request, response);
        assertThat(results).hasSize(1);
        assertThat(results[0]).isEqualTo(id);
    }

    @DisplayName("파라미터가 숫자")
    @Test
    void resolve_numeric() throws Exception {
        long id = 4131;
        int age = 1236;

        request.addParameter("id", String.valueOf(id));
        request.addParameter("age", String.valueOf(age));

        Method method = getMethod("create_int_long", testUserControllerClass.getDeclaredMethods());

        Object[] results = HandlerMethodArgumentResolver.resolve(method, request, response);
        assertThat(results).hasSize(2);
        assertThat(results[0]).isEqualTo(id);
        assertThat(results[1]).isEqualTo(age);
    }

    @DisplayName("파라미터가 HttpServletRequest, HttpServletResponse")
    @Test
    void request_response() throws Exception {
        Method method = getMethod("request_response", testUserControllerClass.getDeclaredMethods());

        Object[] results = HandlerMethodArgumentResolver.resolve(method, request, response);
        assertThat(results).hasSize(2);
        assertThat(results[0]).isSameAs(request);
        assertThat(results[1]).isSameAs(response);
    }
}
