package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class PathVariableMethodArgumentResolverTest extends ArgumentResolverTest {

    private final ArgumentResolver argumentResolver = new PathVariableMethodArgumentResolver();

    @DisplayName("핸들러 메서드의 인자와 경로 변수의 값을 매핑한다.")
    @Test
    void resolveArgument() throws Exception {
        Class<TestUserController> clazz = TestUserController.class;
        Method method = clazz.getMethod("show_pathvariable", long.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users/100");
        MockHttpServletResponse response = new MockHttpServletResponse();

        MethodParameter[] methodParameters = getMethodParameters(method);
        List<Object> argumentValues = Arrays.stream(methodParameters)
            .map(parameter -> argumentResolver.resolveArgument(parameter, request, response))
            .collect(Collectors.toList());

        assertThat(argumentValues).hasSize(1);
        assertThat((long) argumentValues.get(0)).isEqualTo(100L);
    }
}
