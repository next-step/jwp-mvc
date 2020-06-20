package core.mvc.support.resolvers;

import core.annotation.web.CookieValue;
import core.annotation.web.PathVariable;
import core.mvc.support.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.Cookie;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ResolversTest {

    private final String NAME = "hyeyoom";
    private final String PASSWORD = "1234abcd";
    private final long AGE = 100;
    private final String COOKIE_KEY = "sessionId";
    private final String COOKIE_VALUE = UUID.randomUUID().toString();
    private final String PATH_VALUE = "1024";

    private MethodSignature methodSignature;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() throws Exception {
        request = new MockHttpServletRequest("POST", "/test/" + PATH_VALUE);
        request.addParameter("name", NAME);
        request.addParameter("password", PASSWORD);
        request.addParameter("age", String.valueOf(AGE));
        final Cookie cookie = new Cookie(COOKIE_KEY, COOKIE_VALUE);
        request.setCookies(cookie);

        final Method method = ResolversTest.class.getMethod("testMethod", TestObject.class, String.class, long.class, String.class);
        methodSignature = new MethodSignature(method);
    }

    @DisplayName("핸들러 리졸버 테스트")
    @Test
    void test_resolvers() {
        final Object[] expected = new Object[4];
        final TestObject to = new TestObject(NAME, PASSWORD, AGE);
        expected[0] = to;
        expected[1] = COOKIE_VALUE;
        expected[2] = Long.parseLong(PATH_VALUE);
        expected[3] = NAME;

        final Object[] actual = Resolvers.resolveArguments(methodSignature, request);

        assertThat(expected).isEqualTo(actual);
    }

    public void testMethod(TestObject testObject, @CookieValue String sessionId, @PathVariable long id, String name) {
        // no-op
    }

    public static class TestObject {
        private String name;
        private String password;
        private long age;

        public TestObject() {
        }

        public TestObject(String name, String password, long age) {
            this.name = name;
            this.password = password;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestObject that = (TestObject) o;
            return age == that.age &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(password, that.password);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, password, age);
        }

        @Override
        public String toString() {
            return "TestObject{" +
                    "name='" + name + '\'' +
                    ", password='" + password + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}