package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.mvc.MethodParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class PathVariableArgumentResolverTest extends TestCase {

    private PathVariableArgumentResolver pathVariableArgumentResolver;

    @BeforeEach
    void setUp() {
        pathVariableArgumentResolver = new PathVariableArgumentResolver();
    }

    @DisplayName("@PathVariable이 있는 메서드는 지원한다")
    @Test
    void pathVariableAnnotation() throws NoSuchMethodException {

        Method method = TargetClass.class.getDeclaredMethod("pathVariableMethod", String.class);

        PathVariable pathVariable = method.getParameters()[0].getAnnotation(PathVariable.class);

        MethodParameter methodParameter = new MethodParameter("id", String.class, new Annotation[]{pathVariable}, method);
        assertThat(pathVariableArgumentResolver.supports(methodParameter)).isTrue();
    }

    @DisplayName("@RequestParam은 지원하지 않는다")
    @Test
    void notSupportRequestParamAnnotation() throws NoSuchMethodException {

        Method method = TargetClass.class.getDeclaredMethod("requestParamMethod", String.class);
        RequestParam requestParam = method.getAnnotation(RequestParam.class);

        MethodParameter methodParameter = new MethodParameter("id", String.class, new Annotation[]{requestParam}, method);
        assertThat(pathVariableArgumentResolver.supports(methodParameter)).isFalse();
    }

    @DisplayName("@PathVariable에 올바른 값이 들어가는지 확인")
    @Test
    void pathVariableExtractVariable() throws NoSuchMethodException {
        Method method = TargetClass.class.getDeclaredMethod("pathVariableMethod", String.class);
        PathVariable pathVariable = method.getParameters()[0].getAnnotation(PathVariable.class);

        MethodParameter methodParameter = new MethodParameter("id", String.class, new Annotation[]{pathVariable}, method);
        MockHttpServletRequest request = new MockHttpServletRequest(RequestMethod.GET.name(), "/test/10");
        Object object = pathVariableArgumentResolver.resolveArgument(methodParameter, request, new MockHttpServletResponse());

        assertThat(object).isInstanceOf(String.class);
        assertThat((String)object).isEqualTo("10");
    }

}