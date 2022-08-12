package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import core.mvc.ModelAndView;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;

class HandlerMethodArgumentResolverTest {

    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodArgumentResolverTest.class);
    private final HandlerMethodArgumentResolver handlerMethodArgumentResolver = new HandlerMethodArgumentResolver();

    private final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Test
    void string() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String userId = "javajigi";
        String password = "password";
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        Class<?> clazz = TestUserController.class;
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

    @DisplayName("메소드의 파라미터 타입의 Object 배열을 반환한다")
    @Test
    void returns_an_object_array_of_parameter_types_of_method() {
        // given
        final HandlerMethodArgumentResolver handlerMethodArgumentResolver = new HandlerMethodArgumentResolver();

        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        request.addParameter("userId", "admin");
        request.addParameter("password", "pass");

        final Class<?> clazz = TestUserController.class;
        final Method method = getMethod("create_string", clazz.getDeclaredMethods());

        // when
        final Object[] actual = handlerMethodArgumentResolver.resolve(method, request);

        // then
        assertThat(actual).containsExactly("admin", "pass");

    }

    @DisplayName("메소드의 파라미터가 없으면 빈 Object 배열을 반환한다")
    @Test
    void returns_an_empty_object_array_of_does_not_have_parameters_method() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/empty-parameters");
        request.addParameter("userId", "admin");
        request.addParameter("password", "pass");

        final Class<?> clazz = TestUserController.class;
        final Method method = getMethod("notParameters", clazz.getDeclaredMethods());

        // when
        final Object[] actual = handlerMethodArgumentResolver.resolve(method, request);

        // then
        assertThat(actual).isEmpty();

    }

    @DisplayName("메소드의 파라미터가 원시 타입인 Object 배열을 반환한다")
    @Test
    void returns_an_object_array_of_primitive_types_of_method() throws Exception {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/empty-parameters");
        request.addParameter("id", String.valueOf(Long.MAX_VALUE));
        request.addParameter("age", String.valueOf(Integer.MAX_VALUE));

        final Class<?> clazz = TestUserController.class;
        final Method method = getMethod("create_int_long", clazz.getDeclaredMethods());

        // when
        final Object[] actual = handlerMethodArgumentResolver.resolve(method, request);

        // then
        assertAll(
            () -> assertThat(actual[0]).isEqualTo(Long.MAX_VALUE),
            () -> assertThat(actual[1]).isEqualTo(Integer.MAX_VALUE)
        );
    }
}
