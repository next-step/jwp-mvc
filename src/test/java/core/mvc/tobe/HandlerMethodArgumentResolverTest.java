package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.tobe.argumentresolver.MethodArgumentHandler;
import core.mvc.tobe.argumentresolver.NumberArgumentResolver;
import core.mvc.tobe.argumentresolver.UserArgumentResolver;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerMethodArgumentResolverTest {
    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodArgumentResolverTest.class);
    private static final String TEST_USER_ID = "javajigi";
    private static final String TEST_USER_PASSWORD = "password";
    private static final String TEST_USER_NAME = "name";
    private static final String TEST_USER_EMAIL = "email@naver.com";
    private static final String USER_ID_PARAMETER_NAME = "userId";
    private static final String USER_PASSWORD_PARAMETER_NAME = "password";
    private static final String USER_NAME_PARAMETER_NAME = "name";
    private static final String USER_EMAIL_PARAMETER_NAME = "email";

    private MethodArgumentHandler methodArgumentHandler = new MethodArgumentHandler();

    @BeforeEach
    void setUp() {
        methodArgumentHandler.add(new NumberArgumentResolver());
        methodArgumentHandler.add(new UserArgumentResolver());
    }

    @Test
    @DisplayName("String 파라미터를 파싱 하는지 확인한다.")
    void name() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(USER_ID_PARAMETER_NAME, TEST_USER_ID);
        request.addParameter(USER_PASSWORD_PARAMETER_NAME, TEST_USER_PASSWORD);
        Class clazz = TestUserController.class;
        Method method = getMethod("create_string", clazz.getDeclaredMethods());

        Object[] values = methodArgumentHandler.getValues(method, request);
        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject(USER_ID_PARAMETER_NAME)).isEqualTo(TEST_USER_ID);
        assertThat(mav.getObject(USER_PASSWORD_PARAMETER_NAME)).isEqualTo(TEST_USER_PASSWORD);
    }

    @Test
    @DisplayName("User 파라미터를 파싱 하는지 확인한다.")
    void userArgumentMapping() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        String userId = TEST_USER_ID;
        String password = TEST_USER_PASSWORD;
        String name = TEST_USER_NAME;
        String email = TEST_USER_EMAIL;

        MockHttpServletRequest request = getMockHttpServletRequest(userId, password, name, email);
        User user = new User(userId, password, name, email);
        Class clazz = TestUserController.class;
        Method method = getMethod("create_javabean", clazz.getDeclaredMethods());

        Object[] values = methodArgumentHandler.getValues(method, request);
        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("testUser")).isEqualTo(user);

    }

    private MockHttpServletRequest getMockHttpServletRequest(String userId, String password, String name, String email) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(USER_ID_PARAMETER_NAME, userId);
        request.addParameter(USER_PASSWORD_PARAMETER_NAME, password);
        request.addParameter(USER_NAME_PARAMETER_NAME, name);
        request.addParameter(USER_EMAIL_PARAMETER_NAME, email);
        return request;
    }

    @Test
    @DisplayName("원시형 파라미터를 파싱 하는지 확인한다.")
    void primitiveArgumentMapping() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Integer id = 1;
        int age = 10;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", String.valueOf(id));
        request.addParameter("age", String.valueOf(age));

        Class clazz = TestUserController.class;
        Method method = getMethod("create_int_long", clazz.getDeclaredMethods());

        Object[] values = methodArgumentHandler.getValues(method, request);
        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("id")).isEqualTo(id);
        assertThat(mav.getObject("age")).isEqualTo(age);

    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
