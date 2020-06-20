package core.mvc.support.resolvers;

import core.mvc.support.MethodParameter;
import core.mvc.support.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaBeanHandlerMethodArgumentResolverTest {

    private static final HandlerMethodArgumentResolver resolver = new JavaBeanHandlerMethodArgumentResolver();
    private MethodParameter methodParameter;

    @BeforeEach
    void setUp() throws Exception {
        final Method method = JavaBeanHandlerMethodArgumentResolverTest.class.getMethod("testMethod", JavaBean.class);
        final MethodSignature methodSignature = new MethodSignature(method);
        methodParameter = methodSignature.getMethodParameters().get(0);
    }

    // primitive 타입도 아니고, 어노테이션도 없는 경우
    @DisplayName("자바빈 리졸버의 지원 조건을 테스트한다.")
    @Test
    void test_support_condition() {
        assertTrue(resolver.supportsParameter(methodParameter));
    }

    @DisplayName("자바빈을 잘 해석하는지 테스트한다.")
    @Test
    void test_resolve_argument() {
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/random");
        final String answerId = "hyeyoom";
        request.addParameter("userId", "hyeyoom");
        final String answerPassword = "1234abcd";
        request.addParameter("password", "1234abcd");
        final long answerAge = 100;
        request.addParameter("age", String.valueOf(answerAge));

        final JavaBean bean = new JavaBean(answerId, answerPassword, answerAge);
        final JavaBean argument = (JavaBean) resolver.resolveArgument(methodParameter, request);
        assertThat(bean).isEqualTo(argument);
    }

    public void testMethod(JavaBean bean) {
        // no-op
    }

    public static class JavaBean {

        private final String userId;
        private final String password;
        private final long age;

        public JavaBean(String userId, String password, long age) {
            this.userId = userId;
            this.password = password;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JavaBean javaBean = (JavaBean) o;
            return age == javaBean.age &&
                    Objects.equals(userId, javaBean.userId) &&
                    Objects.equals(password, javaBean.password);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, password, age);
        }
    }
}