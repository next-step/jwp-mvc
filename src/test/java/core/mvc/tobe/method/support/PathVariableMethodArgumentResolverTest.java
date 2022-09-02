package core.mvc.tobe.method.support;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.di.factory.example.MockView;
import core.mvc.ModelAndView;
import core.mvc.tobe.method.MethodParameter;
import core.mvc.tobe.method.SimpleTypeConverter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@DisplayName("@PathVariable로 메서드 인자 추출")
class PathVariableMethodArgumentResolverTest {

    public static final PathVariableMethodArgumentResolver RESOLVER =
            new PathVariableMethodArgumentResolver(SimpleTypeConverter.getTypeConverter());

    @DisplayName("객체 생성")
    @Test
    void instance() {
        assertThatNoException().isThrownBy(
                () -> new PathVariableMethodArgumentResolver(SimpleTypeConverter.getTypeConverter()));
    }

    @DisplayName("객체 생성 실패")
    @Test
    void missing_type_converter_param() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new PathVariableMethodArgumentResolver(null));
    }

    @DisplayName("@RequestMapping 메서드에 @PathVariable 인자가 존재해야만 한다.")
    @Test
    void request_mapping_path_variable_require() throws NoSuchMethodException {
        assertThat(RESOLVER.supportsParameter(userIdParamWithPathVariableMethodParam())).isTrue();
    }

    @DisplayName("@PathVariable이 인자에 없으면 지원하지 않는다.")
    @Test
    void no_support_without_path_variable() throws NoSuchMethodException {
        assertThat(RESOLVER.supportsParameter(paramWithoutPathVariableMethodParam())).isFalse();
    }

    @DisplayName("URL 경로로 인자 추출")
    @Test
    void resolveArgument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users/1");

        Object result = RESOLVER.resolveArgument(
                userIdParamWithPathVariableMethodParam(), request, new MockHttpServletResponse());

        assertThat(result).isEqualTo(1L);
    }

    private MethodParameter paramWithoutPathVariableMethodParam() throws NoSuchMethodException {
        Method method = PathVariableMethodArgumentResolverTest.class.
                getDeclaredMethod("paramWithoutPathVariable", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    @RequestMapping
    private ModelAndView paramWithoutPathVariable(long userId) {
        ModelAndView modelAndView = new ModelAndView(new MockView());
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

    private MethodParameter userIdParamWithPathVariableMethodParam() throws NoSuchMethodException {
        Method method = PathVariableMethodArgumentResolverTest.class.
                getDeclaredMethod("userIdParameterWithPathVariable", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    private ModelAndView userIdParameterWithPathVariable(@PathVariable("userId") long userId) {
        ModelAndView modelAndView = new ModelAndView(new MockView());
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }
}
