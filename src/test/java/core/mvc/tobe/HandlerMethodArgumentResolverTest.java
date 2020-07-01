package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerMethodArgumentResolverTest {
    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodArgumentResolverTest.class);

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();


    @Test
    public void usersPostString() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        String userId = "test";
        String password = "password";
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        ModelAndView modelAndView = executeMethod(request);
        assertThat(modelAndView.getObject("userId")).isEqualTo(userId);
        assertThat(modelAndView.getObject("password")).isEqualTo(password);
    }

    @Test
    public void usersPostPrimitive() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        long id = 1;
        int age = 30;
        request.addParameter("id", Long.toString(id));
        request.addParameter("age", Integer.toString(age));

        ModelAndView modelAndView = executeMethod(request);
        String modelId = Objects.toString(modelAndView.getObject("id"));
        String modelAge = Objects.toString(modelAndView.getObject("age"));
        assertThat(Long.parseLong(modelId)).isEqualTo(id);
        assertThat(Integer.parseInt(modelAge)).isEqualTo(age);
    }

    @Test
    public void usersPostObject() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        TestUser testUser = new TestUser("test", "pass", 30);

        request.addParameter("userId", testUser.getUserId());
        request.addParameter("password", testUser.getPassword());
        request.addParameter("age", Integer.toString(testUser.getAge()));

        ModelAndView modelAndView = executeMethod(request);
        assertThat(modelAndView.getObject("testUser")).isEqualToComparingFieldByField(testUser);
    }


    private ModelAndView executeMethod(HttpServletRequest request) throws Exception {
        Class clazz = TestUserController.class;

        Method method = getMethod(request.getRequestURI(), clazz.getDeclaredMethods(), request.getParameterMap().keySet());

        logger.debug("{}", request.getParameterMap().keySet());

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> "/users".equals(m.getAnnotation(RequestMapping.class).value()))
                .forEach(m -> {
                    logger.debug("{}", m.getName());

                    String[] parameterNames = nameDiscoverer.getParameterNames(m);
                    Arrays.stream(parameterNames).forEach(p -> {
                        logger.debug("{}, {}", p, request.getParameterMap().containsKey(p));
                    });
                    logger.debug("========\n");
                });

        Object[] values = getParameters(request, method);

        return (ModelAndView) method.invoke(clazz.newInstance(), values);
    }

    private Method getMethod(String name, Method[] methods, Set<String> parameterNames) {
        return Arrays.stream(methods)
                .filter(m -> name.equals(m.getAnnotation(RequestMapping.class).value()))
                .filter(m -> isMatchParameters(m, parameterNames))
                .findFirst()
                .get();
    }

    private boolean isMatchParameters(Method method, Set<String> requestParameterNames) {
        String[] methodParameterNames = nameDiscoverer.getParameterNames(method);
        int matchingCount = Arrays.stream(methodParameterNames)
                .filter(p -> requestParameterNames.contains(p))
                .collect(Collectors.toList()).size();

        return matchingCount == methodParameterNames.length;
    }

    private Object[] getParameters(HttpServletRequest request, Method method) {
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            logger.debug("parameter : {}", parameterName);

            String parameterValue = request.getParameter(parameterName);
            Class<?> parameterType = method.getParameterTypes()[i];

            values[i] = getParameterValue(parameterValue, parameterType);

            logger.debug("= : {}", values[i]);
        }

        return values;
    }

    private Object getParameterValue(String parameterValue, Class<?> parameterType) {
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(parameterValue);
        }

        if (parameterType.equals(long.class)) {
            return Long.parseLong(parameterValue);
        }

        return parameterValue;
    }
}
