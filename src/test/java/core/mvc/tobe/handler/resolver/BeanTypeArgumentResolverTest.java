package core.mvc.tobe.handler.resolver;

import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;

class BeanTypeArgumentResolverTest {
    private static final NamedParameter BEAN_TYPE_PARAMETER;


    private final BeanTypeArgumentResolver argumentResolver = new BeanTypeArgumentResolver();

    static {
        Method testClassMethod = TestClass.class.getDeclaredMethods()[0];
        Parameter[] parameters = testClassMethod.getParameters();

        BEAN_TYPE_PARAMETER = new NamedParameter(parameters[0], "testUser");
    }

    @Test
    void name() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "userId");
        request.addParameter("password", "password");
        request.addParameter("age", "20");

        Object actual = argumentResolver.resolve(BEAN_TYPE_PARAMETER, request, new MockHttpServletResponse());

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new TestUser(
                        "userId",
                        "password",
                        20
                ));
    }

    private static class TestClass {
        public void test(TestUser testUser) {

        }
    }
}
