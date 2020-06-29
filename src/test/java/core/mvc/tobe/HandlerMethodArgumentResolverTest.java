package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.server.PathContainer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerMethodArgumentResolverTest {
    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodArgumentResolverTest.class);

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Test
    void string() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
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

    @Test
    void intAndLong() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        long id = 1L;
        int age = 29;
        request.setParameter("id", String.valueOf(id));
        request.setParameter("age", String.valueOf(age));

        Class clazz = TestUserController.class;
        Method method = getMethod("create_int_long", clazz.getDeclaredMethods());

        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            String value = request.getParameter(parameterName);
            values[i] = value;
            for (final Class<?> parameterType : method.getParameterTypes()) {
                if (parameterType.equals(int.class)) {
                    values[i] = Integer.parseInt(value);
                }
                if (parameterType.equals(long.class)) {
                    values[i] = Long.parseLong(value);
                }
            }
        }
        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("id")).isInstanceOf(Long.class);
        assertThat(mav.getObject("id")).isEqualTo(id);
        assertThat(mav.getObject("age")).isInstanceOf(Integer.class);
        assertThat(mav.getObject("age")).isEqualTo(age);
    }

    @Test
    void pathVariable() throws Exception {
        Class clazz = TestUserController.class;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users/1");

        for (final Method method : clazz.getMethods()) {
            final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            PathPattern pp = parse(annotation.value());
            if (pp.matches(toPathContainer(request.getRequestURI()))) {
                final Map<String, String> uriVariables = pp.matchAndExtract(toPathContainer(request.getRequestURI()))
                        .getUriVariables();

                String[] parameterNames = nameDiscoverer.getParameterNames(method);
                Object[] values = new Object[parameterNames.length];
                for (int i = 0; i < parameterNames.length; i++) {
                    String parameterName = parameterNames[i];
                    String value = uriVariables.get(parameterName);
                    values[i] = value;

                    for (final Class<?> parameterType : method.getParameterTypes()) {
                        if (parameterType.equals(int.class)) {
                            values[i] = Integer.parseInt(value);
                        }
                        if (parameterType.equals(long.class)) {
                            values[i] = Long.parseLong(value);
                        }
                    }
                }
                ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
                assertThat(mav.getObject("id")).isInstanceOf(Long.class);
                assertThat(mav.getObject("id")).isEqualTo(1L);
            }
        }
    }

    @Test
    void object() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("userId", "123");
        request.setParameter("password", "556");
        request.setParameter("age", "177");
        Class clazz = TestUserController.class;

        Method method = getMethod("create_javabean", clazz.getDeclaredMethods());

        for (final Class<?> object : method.getParameterTypes()) {
            final Field[] declaredFields = object.getDeclaredFields();
            Object[] values = new Object[declaredFields.length];
            for (int i = 0; i < declaredFields.length; i++) {
                String value = request.getParameter(declaredFields[i].getName());
                values[i] = value;
                if (declaredFields[i].getType().equals(int.class)) {
                    values[i] = Integer.parseInt(value);
                }

                if (declaredFields[i].getType().equals(long.class)) {
                    values[i] = Long.parseLong(value);
                }
            }

            Constructor<?> declaredConstructor = Arrays.stream(object.getConstructors())
                    .max((o1, o2) -> Math.max(o1.getParameterCount(), o2.getParameterCount()))
                    .get();

            declaredConstructor.setAccessible(true);
            TestUser user = (TestUser) declaredConstructor.newInstance(values);
            assertThat(user.getAge()).isEqualTo(177);
            assertThat(user.getUserId()).isEqualTo("123");
            assertThat(user.getPassword()).isEqualTo("556");
            ModelAndView modelAndView = (ModelAndView) method.invoke(clazz.newInstance(), user);
            assertThat(modelAndView.getObject("testUser")).isInstanceOf(TestUser.class);
        }
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
