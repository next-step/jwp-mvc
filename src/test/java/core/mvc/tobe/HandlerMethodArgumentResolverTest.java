package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.tobe.resolver.HandlerMethodArgumentResolver;
import core.mvc.tobe.resolver.JavaBeanArguementResolver;
import core.mvc.tobe.resolver.RequestParameterArgumentResolver;
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

    public Object[] getMethodExecuteParameter(HttpServletRequest request, Method method) {
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            Parameter parameter = method.getParameters()[i];
            String parameterName = parameterNames[i];
            Class<?> parameterType = parameter.getType();

            Object value = null;
            HandlerMethodArgumentResolver resolver = new RequestParameterArgumentResolver();
            HandlerMethodArgumentResolver resolver2 = new JavaBeanArguementResolver();

            if (resolver.isSupport(parameterType)) {
                value = resolver.resolve(request, parameterName, parameterType);
            }
            if (resolver2.isSupport(parameterType)) {
                value = resolver2.resolve(request, parameterName, parameterType);
            }


            values[i] = value;
        }

        return values;
    }

}
