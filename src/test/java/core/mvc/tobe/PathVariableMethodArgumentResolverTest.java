package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Annotation annotation = parameterAnnotations[0][0];

        MethodParameter methodParameter = new MethodParameter(long.class, method, "id", new Annotation[]{annotation});
        Object argument = argumentResolver.resolveArgument(methodParameter, request, response);

        assertThat((long) argument).isEqualTo(100L);
    }
}
