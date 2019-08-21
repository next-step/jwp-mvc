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

class RequestParamMethodArgumentResolverTest extends TestCase {
    private RequestParamMethodArgumentResolver requestParamMethodArgumentResolver;

    @BeforeEach
    void setUp() {
        requestParamMethodArgumentResolver = new RequestParamMethodArgumentResolver();
    }

    @DisplayName("@RequestParam은 지원한다")
    @Test
    void 지원하는메서드파라미터() throws NoSuchMethodException {
        Method method = TargetClass.class.getDeclaredMethod("requestParamMethod", String.class);
        RequestParam requestParam = method.getParameters()[0].getAnnotation(RequestParam.class);
        MethodParameter methodParameter = new MethodParameter("id", String.class, new Annotation[]{requestParam}, method);

        assertThat(requestParamMethodArgumentResolver.supports(methodParameter)).isTrue();
    }

    @DisplayName("@PathVariable은 지원하지 않는다")
    @Test
    void 지원하지않는메서드파라미터() throws NoSuchMethodException {
        Method method = TargetClass.class.getDeclaredMethod("pathVariableMethod", String.class);
        PathVariable pathVariable = method.getParameters()[0].getAnnotation(PathVariable.class);
        MethodParameter methodParameter = new MethodParameter("id", String.class, new Annotation[]{pathVariable}, method);

        assertThat(requestParamMethodArgumentResolver.supports(methodParameter)).isFalse();
    }

    @DisplayName("@RequestParam으로 값이 잘 넘어갔는지")
    @Test
    void RequestParamParsing() throws NoSuchMethodException {
        Method method = TargetClass.class.getDeclaredMethod("requestParamMethod", String.class);
        RequestParam requestParam = method.getParameters()[0].getAnnotation(RequestParam.class);
        MethodParameter methodParameter = new MethodParameter("id", String.class, new Annotation[]{requestParam}, method);

        MockHttpServletRequest request = new MockHttpServletRequest(RequestMethod.GET.name(), "/test");
        request.setParameter("id", "me");

        Object object = requestParamMethodArgumentResolver.resolveArgument(methodParameter, request, new MockHttpServletResponse());

        assertThat(object).isInstanceOf(String.class);
        assertThat((String)object).isEqualTo("me");
    }
}