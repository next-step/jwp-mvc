package core.mvc.tobe;

import core.mvc.MethodParameter;
import core.mvc.resolver.ParameterArgumentResolver;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;
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
    }

    @ParameterizedTest
    @MethodSource("supportingType")
    void supports_test(MethodParameter methodParameter) {
        assertTrue(resolver.supports(methodParameter));
    }

    @DisplayName("Type이 String인 경우")
    @Test
    void get_argument_String() throws Exception {
        request.addParameter("name", "Summer");

        MethodParameter parameter = new MethodParameter("name", String.class);
        String argument = (String) resolver.getMethodArgument(parameter, request, response);

        assertEquals("Summer", argument);
    }

    @DisplayName("Type이 int인 경우")
    @Test
    void get_argument_int() throws Exception {
        request.addParameter("age", "28");

        MethodParameter parameter = new MethodParameter("age", int.class);
        int argument = (int) resolver.getMethodArgument(parameter, request, response);

        assertEquals(28, argument);
    }

    @DisplayName("Type이 Integer인 경우")
    @Test
    void get_argument_integer() throws Exception {
        request.addParameter("age", "28");

        MethodParameter parameter = new MethodParameter("age", Integer.class);
        Integer argument = (Integer) resolver.getMethodArgument(parameter, request, response);

        assertEquals(28, argument);
    }

    @DisplayName("Type이 HttpSession인 경우")
    @Test
    void get_argument_session() throws Exception {
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        MethodParameter parameter = new MethodParameter("session", HttpSession.class);
        HttpSession argument = (HttpSession) resolver.getMethodArgument(parameter, request, response);

        assertEquals(session.getId(), argument.getId());
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
                new MethodParameter("int", int.class),
                new MethodParameter("Integer", Integer.class),
                new MethodParameter("String", String.class),
                new MethodParameter("long", long.class),
                new MethodParameter("Long", Long.class),
                new MethodParameter("boolean", boolean.class),
                new MethodParameter("Boolean", Boolean.class),
                new MethodParameter("byte", byte.class),
                new MethodParameter("Byte", Byte.class),
                new MethodParameter("session", HttpSession.class),
                new MethodParameter("testUser", TestUser.class),
                new MethodParameter("user", User.class)
                );
    }
}