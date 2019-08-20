package core.mvc.resolver;

import core.mvc.MethodParameter;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParameterArgumentResolverTest {

    private ParameterArgumentResolver resolver;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() {
        resolver = new ParameterArgumentResolver();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @ParameterizedTest
    @MethodSource("supportingType")
    void supports_test(MethodParameter methodParameter) {
        assertTrue(resolver.supports(methodParameter));
    }

    @DisplayName("Type이 java bean인 경우")
    @Test
    void get_argument_java_bean() throws Exception {
        TestUser testUser = new TestUser("Summer", "pw", 28);
        request.addParameter("userId", testUser.getUserId());
        request.addParameter("password", testUser.getPassword());
        request.addParameter("age", String.valueOf(testUser.getAge()));

        MethodParameter parameter = new MethodParameter("testUser", TestUser.class);
        TestUser argument = (TestUser) resolver.getMethodArgument(parameter, request, response);

        assertEquals(testUser.getUserId(), argument.getUserId());
        assertEquals(testUser.getPassword(), argument.getPassword());
        assertEquals(testUser.getAge(), argument.getAge());
    }

    private static List<MethodParameter> supportingType() {
        return Arrays.asList(
                new MethodParameter("testUser", TestUser.class),
                new MethodParameter("user", User.class)
                );
    }
}