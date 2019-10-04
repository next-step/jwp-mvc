package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerMethodArgumentResolverTest {
    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodArgumentResolverTest.class);

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Test
    void string() throws Exception {
        ExecuteMethod handlerExecution = new ExecuteMethod();
        MockHttpServletRequest request = new MockHttpServletRequest();
        String userId = "javajigi";
        String password = "password";
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        Class clazz = TestUserController.class;
        Method method = getMethod("createString", clazz.getDeclaredMethods());
        Object[] values = handlerExecution.getParameterValue(request, method);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("userId")).isEqualTo(userId);
        assertThat(mav.getObject("password")).isEqualTo(password);
    }

    @Test
    void intLong() throws Exception {
        ExecuteMethod handlerExecution = new ExecuteMethod();
        MockHttpServletRequest request = new MockHttpServletRequest();
        long id = 1;
        int age = 33;
        request.addParameter("id", String.valueOf(id));
        request.addParameter("age", String.valueOf(age));

        Class clazz = TestUserController.class;
        Method method = getMethod("createIntLong", clazz.getDeclaredMethods());
        Object[] values = handlerExecution.getParameterValue(request, method);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("id")).isEqualTo(id);
        assertThat(mav.getObject("age")).isEqualTo(age);
    }

    @Test
    void initClass() throws Exception {
        ExecuteMethod handlerExecution = new ExecuteMethod();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "aaa");
        request.addParameter("password", "bbb");
        request.addParameter("age", "22");

        Class clazz = TestUserController.class;
        Method method = getMethod("createJavabean", clazz.getDeclaredMethods());
        Object[] values = handlerExecution.getParameterValue(request, method);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(((TestUser)mav.getObject("testUser")).getUserId()).isEqualTo("aaa");
        assertThat(((TestUser)mav.getObject("testUser")).getPassword()).isEqualTo("bbb");
        assertThat(((TestUser)mav.getObject("testUser")).getAge()).isEqualTo(22);
    }

    @Test
    void initPath() throws Exception {
        ExecuteMethod handlerExecution = new ExecuteMethod();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/users/1");

        Class clazz = TestUserController.class;
        Method method = getMethod("showPathVariable", clazz.getDeclaredMethods());
        Object[] values = handlerExecution.getParameterValue(request, method);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("id")).isEqualTo(1L);
    }

    @Test
    void initPath2() throws Exception {
        ExecuteMethod handlerExecution = new ExecuteMethod();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/users/1");
        request.addParameter("password", "bbb");
        request.addParameter("age", "22");

        Class clazz = TestUserController.class;
        Method method = getMethod("showPathVariableSetter", clazz.getDeclaredMethods());
        Object[] values = handlerExecution.getParameterValue(request, method);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(((TestUser)mav.getObject("testUser")).getUserId()).isEqualTo("1");
        assertThat(((TestUser)mav.getObject("testUser")).getPassword()).isEqualTo("bbb");
        assertThat(((TestUser)mav.getObject("testUser")).getAge()).isEqualTo(22);
    }


    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
