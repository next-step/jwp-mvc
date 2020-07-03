package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Constructor;
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
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            Parameter parameter = method.getParameters()[i];
            String parameterName = parameterNames[i];

            logger.debug("parameter : {}", parameterName);

            values[i] = request.getParameter(parameterName);
        }

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
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        logger.debug("{}, {}", parameterNames, parameterNames.length);

        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            Parameter parameter = method.getParameters()[i];
            String parameterName = parameterNames[i];
            logger.debug("parameter : {}", parameterName);

            Class<?> parameterType = parameter.getType();

            String value = Objects.toString(request.getParameter(parameterName));
            if (parameterType.equals(int.class)) {
                values[i] = Integer.parseInt(value);
            }
            if (parameterType.equals(long.class)) {
                values[i] = Long.parseLong(value);
            }

        }

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
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            Parameter parameter = method.getParameters()[i];
            String parameterName = parameterNames[i];
            logger.debug("parameter : {}", parameterName);

            Class<?> parameterType = parameter.getType();

            logger.debug("constructor length : {}", parameterType.getConstructors().length);
            Constructor constructor = parameterType.getConstructors()[0];

            Parameter[] parameters = constructor.getParameters();
            String[] constructorParameterNames = nameDiscoverer.getParameterNames(constructor);
            Object[] constructorValues = new Object[parameters.length];

            logger.debug("requestParam : {}", request.getParameterMap());
            for (int j = 0; j < parameters.length; j++) {
                Class<?> constructorParameterType = parameters[j].getType();


                String constructorParameterName = constructorParameterNames[j];

                String value = request.getParameter(constructorParameterName);
                constructorValues[j] = value;
                if (constructorParameterType.equals(int.class)) {
                    constructorValues[j] = Integer.parseInt(value);
                }
                if (constructorParameterType.equals(long.class)) {
                    constructorValues[j] = Long.parseLong(value);
                }

                logger.debug("constructor : {}, {}, {}", constructorParameterName, constructorParameterType, constructorValues[j]);
            }

            Object obj = constructor.newInstance(constructorValues);
            logger.debug("obj : {}", obj);
            values[i] = obj;

        }
        ModelAndView modelAndView = (ModelAndView) method.invoke(clazz.newInstance(), values);

        assertThat(modelAndView.getObject("testUser")).isEqualToComparingFieldByField(testUser);
    }


}
