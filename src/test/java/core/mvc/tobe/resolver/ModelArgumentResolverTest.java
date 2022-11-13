package core.mvc.tobe.resolver;

import core.annotation.web.RequestParam;
import core.mvc.tobe.MethodParameter;
import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ModelArgumentResolverTest {

    private static final Logger logger = LoggerFactory.getLogger(ModelArgumentResolver.class);

    private  ModelArgumentResolver resolver = new ModelArgumentResolver();
    private Class<?> clazz = MyClass.class;

    @DisplayName("애노테이션이 없는 타입은 모두 지원한다.")
    @Test
    void supportWithNoAnnotationField() {

        Method requireStringMethod = getMethod("requireString", clazz.getDeclaredMethods());
        Parameter requireStringParameter = getParameter(requireStringMethod);

        assertThat(resolver.support(new MethodParameter(requireStringMethod, requireStringParameter))).isTrue();

        Method requireInteger = getMethod("requireInteger", clazz.getDeclaredMethods());
        Parameter requireIntegerParameter = getParameter(requireInteger);
        assertThat(resolver.support(new MethodParameter(requireStringMethod, requireIntegerParameter))).isTrue();

        Method requireDoubleMethod = getMethod("requireDouble", clazz.getDeclaredMethods());
        Parameter requireDoubleParameter = getParameter(requireDoubleMethod);
        assertThat(resolver.support(new MethodParameter(requireStringMethod, requireDoubleParameter))).isTrue();

        Method requireUserMethod = getMethod("requireUser", clazz.getDeclaredMethods());
        Parameter requireUserParameter = getParameter(requireUserMethod);
        assertThat(resolver.support(new MethodParameter(requireUserMethod, requireUserParameter))).isTrue();
    }

    @DisplayName("애노테이션이 있는 타입은 지원하지 않는다.")
    @Test
    void unSupportWithAnnotationField() {

        Method requireStringWithAnnotationMethod = getMethod("requireStringWithAnnotation", clazz.getDeclaredMethods());
        Parameter requireStringWithParameter = getParameter(requireStringWithAnnotationMethod);
        assertThat(resolver.support(new MethodParameter(requireStringWithAnnotationMethod, requireStringWithParameter))).isFalse();
    }

    @DisplayName("모델을 받은 타입으로 잘 변환하여 반환한다.")
    @Test
    void resolveParameterRequiredMethod() {
        Method stringMethod = getMethod("requireString", clazz.getDeclaredMethods());
        Parameter stringParameter = getParameter(stringMethod);
        MethodParameter methodParameter = new MethodParameter(stringMethod, stringParameter);

        String parameterName = "value";
        Class<?> resultType = String.class;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("value", "5");

        Object value = resolver.resolveArgument(methodParameter, request, response, parameterName);

        assertThat(value).isInstanceOf(resultType);
    }

    @DisplayName("객체정보를 전달하면 객체 인스턴스를 만들어 반환한다.")
    @Test
    void resolveUser() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("userId", "userId");
        request.addParameter("password", "password");
        request.addParameter("name", "name");
        request.addParameter("email", "email");

        Method method = getMethod("requireUser", MyClass.class.getDeclaredMethods());
        Parameter parameter = getParameter(method);

        MethodParameter methodParameter = new MethodParameter(method, parameter);

        Object user = resolver.resolveArgument(methodParameter, request, response, "user");

        assertThat(user).isInstanceOf(User.class);
        assertThat(((User)user).getUserId()).isEqualTo("userId");
        assertThat(((User)user).getPassword()).isEqualTo("password");
        assertThat(((User)user).getName()).isEqualTo("name");
        assertThat(((User)user).getEmail()).isEqualTo("email");
    }

    @DisplayName("객체정보를 전달하면 객체 인스턴스를 만들어 반환한다.")
    @Test
    void resolveInvalidParam() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("value", "hun");

        Method method = getMethod("requireInteger", MyClass.class.getDeclaredMethods());
        Parameter parameter = getParameter(method);

        MethodParameter methodParameter = new MethodParameter(method, parameter);

        assertThatThrownBy(() -> resolver.resolveArgument(methodParameter, request, response, "value"))
                .isInstanceOf(NumberFormatException.class);
    }

    private static Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }

    private static Parameter getParameter(Method method) {
        Parameter[] parameters = method.getParameters();
        return parameters[0];
    }

    public class MyClass {

        public void requireString(String value){
            logger.info("string: {}", value);
        }

        public void requireInteger(int value){
            logger.info("string: {}", value);
        }

        public void requireDouble(double value) {
            logger.info("string: {}", value);
        }

        public void requireUser(User value) {
            logger.info("string: {}", value);
        }

        public void requireStringWithAnnotation(@RequestParam String value) {
            logger.info("string: {}", value);
        }
    }
}