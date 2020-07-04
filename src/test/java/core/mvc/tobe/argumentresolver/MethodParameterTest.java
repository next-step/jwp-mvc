package core.mvc.tobe.argumentresolver;

import core.mvc.ModelAndView;
import core.mvc.tobe.TestUserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodParameterTest {
    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @DisplayName("메소드에서 파라미터 추출 - String 타입")
    @Test
    void getParametersWhenString() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //given
        String userId = "javajigi";
        String password = "password";
        MockHttpServletRequest request = createRequest(userId, password);
        Class clazz = TestUserController.class;
        Method method = getMethod(clazz);

        //when
        Object[] values = MethodParameter.getParameters(request, method);
        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);

        //then
        assertThat(values).hasSize(2);
        assertThat(values).contains(userId);
        assertThat(values).contains(password);
    }

    @DisplayName("메소드에서 파라미터 추출 - Primitive 타입")
    @Test
    void getParametersWhenPrimitive() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //given
        long userId = 1L;
        int password = 12345;
        MockHttpServletRequest request = createRequest(String.valueOf(userId), String.valueOf(password));
        Class clazz = TestUserController.class;
        Method method = getMethod(clazz);

        //when
        Object[] values = MethodParameter.getParameters(request, method);
        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);

        //then
        assertThat(values).hasSize(2);
        assertThat(values).contains(userId);
        assertThat(values).contains(password);
    }

    private Method getMethod(Class clazz){
        return findMethod("create_string", clazz.getDeclaredMethods());
    }

    private Method findMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }

    private MockHttpServletRequest createRequest(String parameter1, String parameter2){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", parameter1);
        request.addParameter("password", parameter2);

        return request;
    }
}
