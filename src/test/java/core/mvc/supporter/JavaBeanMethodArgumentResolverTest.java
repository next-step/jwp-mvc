package core.mvc.supporter;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class JavaBeanMethodArgumentResolverTest {

    @Test
    void resolveArgument() throws Exception {
        // given
        String userId = "tester";
        String password = "123123";
        JavaBeanMethodArgumentResolver resolver = new JavaBeanMethodArgumentResolver();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("userId", userId);
        request.setParameter("password", password);

        final Method method = JavaBeanMethodArgumentResolverTest.class.getMethod("test", TestUser.class);
        Parameter parameter = method.getParameters()[0];

        // when
        TestUser actual = (TestUser) resolver.resolveArgument(parameter, method, request, 0);

        // then
        assertThat(actual).isEqualTo(new TestUser(userId, password));
    }

    public void test(TestUser testUser) {
    }

    static class TestUser {

        private String userId;
        private String password;

        public TestUser() {

        }

        public TestUser(String userId, String password) {
            this.userId = userId;
            this.password = password;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestUser testUser = (TestUser) o;
            return Objects.equals(userId, testUser.userId) && Objects.equals(password, testUser.password);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, password);
        }
    }
}
