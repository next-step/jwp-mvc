package core.mvc.tobe.resolver;

import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;

class ModelArgumentResolverTest extends MethodUtils {
    private static MethodParameter methodParameter;
    private static ModelArgumentResolver modelArgumentResolver = new ModelArgumentResolver();

    @BeforeAll
    static void setUp() {
        Class clazz = ModelArgumentResolverTest.class;
        Method method = getMethod("testMethod", clazz.getDeclaredMethods());
        Parameter[] parameters = method.getParameters();

        methodParameter = new MethodParameter(method, parameters[0], 0);
    }

    @Test
    @DisplayName("Model Type 파라미터를 resolve 할 수 있다.")
    void resolveArgument() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String userId = "wu2ee";
        String password = "password";
        String stringAge = "28";
        request.addParameter("userId", userId);
        request.addParameter("password", password);
        request.addParameter("age", stringAge);
        TestUser expectedTestUser = new TestUser(userId, password, 28);

        // when
        Object resolvedArgument = modelArgumentResolver.resolveArgument(methodParameter, request, response);

        // then
        assertThat(resolvedArgument).isEqualTo(expectedTestUser);
    }

    public void testMethod(TestUser testUser) {
    }
}
