package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import core.mvc.tobe.resolver.ArgumentResolvers;
import core.mvc.tobe.resolver.HandlerMethodArgumentResolver;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
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
            Parameter parameter = method.getParameters()[i];
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

    @Test
    public void usersPostString() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        String userId = "test";
        String password = "password";
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        Class clazz = TestUserController.class;
        Method method = getMethod("create_string", clazz.getDeclaredMethods());

        Object[] values = getMethodExecuteParameter(request, method);

        ModelAndView modelAndView = (ModelAndView) method.invoke(clazz.newInstance(), values);
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

        Class clazz = TestUserController.class;
        Method method = getMethod("create_int_long", clazz.getDeclaredMethods());

        Object[] values = getMethodExecuteParameter(request, method);

        ModelAndView modelAndView = (ModelAndView) method.invoke(clazz.newInstance(), values);

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

        Class clazz = TestUserController.class;
        Method method = getMethod("create_javabean", clazz.getDeclaredMethods());

        Object[] values = getMethodExecuteParameter(request, method);

        ModelAndView modelAndView = (ModelAndView) method.invoke(clazz.newInstance(), values);

        assertThat(modelAndView.getObject("testUser")).isEqualToComparingFieldByField(testUser);
    }

    @Test
    public void usersPostPathVariable() throws Exception {
        long id = 3;
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/" + id);

        Class clazz = TestUserController.class;
        Method method = getMethod("show_pathvariable", clazz.getDeclaredMethods());

//        Object[] values = getMethodExecuteParameter(request, method);
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            Parameter parameter = method.getParameters()[i];
            String parameterName = parameterNames[i];
            Class<?> parameterType = parameter.getType();

            logger.debug("{} {}", parameterName, parameter.isAnnotationPresent(PathVariable.class));

            String uri = method.getAnnotation(RequestMapping.class).value();
            int startIndex = uri.indexOf("{" + parameterName + "}");
            int endIndex = request.getRequestURI().indexOf("/", startIndex);

            logger.debug("{} {}", startIndex, endIndex);
            String str = request.getRequestURI().substring(startIndex);
            if (endIndex >= 0) {
                str = request.getRequestURI().substring(startIndex, endIndex);
            }

            Object value = str;
            if (parameterType == int.class) {
                value = Integer.parseInt(str);
            }
            if (parameterType == long.class) {
                value = Long.parseLong(str);
            }

            logger.debug("{} {}", str, value);
            values[i] = value;
        }

        ModelAndView modelAndView = (ModelAndView) method.invoke(clazz.newInstance(), values);

        assertThat(modelAndView.getObject("id")).isEqualTo(id);
    }

    private Object[] getMethodExecuteParameter(HttpServletRequest request, Method method) {

        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            Parameter parameter = method.getParameters()[i];
            String parameterName = parameterNames[i];
            Class<?> parameterType = parameter.getType();

            HandlerMethodArgumentResolver resolver = ArgumentResolvers.getResolver(parameterType);

            Object value = resolver.resolve(request, parameterName, parameterType);

            values[i] = value;
        }

        return values;
    }

}
