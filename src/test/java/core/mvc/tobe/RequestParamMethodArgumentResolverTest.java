package core.mvc.tobe;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class RequestParamMethodArgumentResolverTest extends ArgumentResolverTest {

    private final ArgumentResolver argumentResolver = new RequestParamMethodArgumentResolver();

    @DisplayName("핸들러 메서드의 String 타입 인자를 매핑한다.")
    @Test
    void stringType() throws Exception {
        Class<TestUserController> clazz = TestUserController.class;
        Method method = clazz.getMethod("create_string", String.class, String.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "javajigi");
        request.addParameter("password", "1234");
        MockHttpServletResponse response = new MockHttpServletResponse();

        MethodParameter[] methodParameters = getMethodParameters(method);
        List<Object> argumentValues = Arrays.stream(methodParameters)
            .map(parameter -> argumentResolver.resolveArgument(parameter, request, response))
            .collect(Collectors.toList());

        assertThat(argumentValues).contains("javajigi", "1234");
    }

    @DisplayName("핸들러 메서드의 long, int 타입 인자를 매핑한다.")
    @Test
    void numberType() throws Exception {
        Class<TestUserController> clazz = TestUserController.class;
        Method method = clazz.getMethod("create_int_long", long.class, int.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", "999");
        request.addParameter("age", "33");
        MockHttpServletResponse response = new MockHttpServletResponse();

        MethodParameter[] methodParameters = getMethodParameters(method);
        List<Object> argumentValues = Arrays.stream(methodParameters)
            .map(parameter -> argumentResolver.resolveArgument(parameter, request, response))
            .collect(Collectors.toList());

        assertThat(argumentValues).contains(999L, 33);
    }
}
