package core.mvc.resolver;

import core.mvc.MethodParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JavaDataTypeArgumentResolverTest {

    private JavaDataTypeArgumentResolver resolver;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() {
        resolver = new JavaDataTypeArgumentResolver();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
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
                new MethodParameter("Byte", Byte.class)
        );
    }
}