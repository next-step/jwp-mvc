package core.mvc.resolver;

import core.annotation.web.RequestParam;
import core.mvc.MethodParameter;
import core.mvc.exception.ArgumentResolverException;
import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NoAnnotationArgumentResolverTest {
    private static final Logger logger = LoggerFactory.getLogger(NoAnnotationArgumentResolverTest.class);

    private NoAnnotationArgumentResolver resolver = new NoAnnotationArgumentResolver();
    private Class<?> clazz = MyClass.class;


    @DisplayName("애노테이션이 없는 타입은 모두 지원한다.")
    @Test
    void supportWithNoAnnotationField() {
        assertThat(resolver.support(
                MethodParameter.of(getMethod("requireString", clazz.getDeclaredMethods()), 0))).isTrue();

        assertThat(resolver.support(
                MethodParameter.of(getMethod("requireInteger", clazz.getDeclaredMethods()), 0))).isTrue();

        assertThat(resolver.support(
                MethodParameter.of(getMethod("requireDouble", clazz.getDeclaredMethods()), 0))).isTrue();

        assertThat(resolver.support(
                MethodParameter.of(getMethod("requireUser", clazz.getDeclaredMethods()), 0))).isTrue();
    }

    @DisplayName("애노테이션이 있는 타입은 지원하지 않는다.")
    @Test
    void unSupportWithAnnotationField() {
        assertThat(resolver.support(
                MethodParameter.of(getMethod("requireStringWithAnnotation", clazz.getDeclaredMethods()), 0)))
                .isFalse();
    }

    @DisplayName("메서드에서 필요로하는 적절한 값으로 변환하여 반환한다.")
    @ParameterizedTest
    @MethodSource("provideMethodParameterAndEtc")
    void resolveParameterRequiredMethod(MethodParameter mp, String parameterName, Class<?> resultType) throws ArgumentResolverException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resq = new MockHttpServletResponse();
        req.addParameter("value", "5");

        Object value = resolver.resolve(req, resq, parameterName, mp);

        assertThat(value).isInstanceOf(resultType);
    }

    public static Stream<Arguments> provideMethodParameterAndEtc() {
        Class<?> clazz = MyClass.class;
        return Stream.of(
                Arguments.of(MethodParameter.of(getMethod("requireString", clazz.getDeclaredMethods()), 0),
                        "value",
                        String.class),

                Arguments.of(MethodParameter.of(getMethod("requireInteger", clazz.getDeclaredMethods()), 0),
                        "value",
                        Integer.class),

                Arguments.of(MethodParameter.of(getMethod("requireDouble", clazz.getDeclaredMethods()), 0),
                        "value",
                        Double.class)
        );
    }

    @DisplayName("유효한 객체 정보를 전달하면 객체 인스턴스를 만들어 반환한다.")
    @Test
    void resolveWithUser() throws ArgumentResolverException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resq = new MockHttpServletResponse();
        req.addParameter("userId", "userId");
        req.addParameter("password", "password");
        req.addParameter("name", "name");
        req.addParameter("email", "email");

        MethodParameter mp = MethodParameter.of(getMethod("requireUser", clazz.getDeclaredMethods()), 0);

        Object user = resolver.resolve(req, resq, "user", mp);

        assertThat(user).isInstanceOf(User.class);
        assertThat(((User)user).getUserId()).isEqualTo("userId");
        assertThat(((User)user).getPassword()).isEqualTo("password");
        assertThat(((User)user).getName()).isEqualTo("name");
        assertThat(((User)user).getEmail()).isEqualTo("email");
    }


    @DisplayName("유효하지 못한 값을 전달하면 예외를 던진다")
    @Test
    void resolveWithInvalidParameter() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resq = new MockHttpServletResponse();
        req.addParameter("value", "hansol");

        MethodParameter mp = MethodParameter.of(getMethod("requireInteger", clazz.getDeclaredMethods()), 0);

        assertThatThrownBy(()-> resolver.resolve(req, resq, "value", mp))
                .isInstanceOf(NumberFormatException.class);
    }


    private static Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
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

        }
    }
}
