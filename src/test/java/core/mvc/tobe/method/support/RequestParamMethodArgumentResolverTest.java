package core.mvc.tobe.method.support;

import core.annotation.web.RequestParam;
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
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("@RequestParam 메서드 인자 추출")
class RequestParamMethodArgumentResolverTest {

    @DisplayName("객체 생성")
    @Test
    void instance() {
        assertThatNoException().isThrownBy(
                () -> RequestParamMethodArgumentResolver.of(true, SimpleTypeConverter.getTypeConverter()));
    }

    @DisplayName("객체 생성 실패")
    @Test
    void instance_fail() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> RequestParamMethodArgumentResolver.of(true, null));
    }

    @DisplayName("인자 추출 - @RequestParam 필수")
    @Test
    void resolveArgument_required_true() throws NoSuchMethodException {
        RequestParamMethodArgumentResolver resolver =
                RequestParamMethodArgumentResolver.of(true, SimpleTypeConverter.getTypeConverter());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "1");

        assertThat(resolver.resolveArgument(paramWithRequestParamMethodParam(), request, new MockHttpServletResponse()))
                .isEqualTo(1L);
    }

    @DisplayName("인자 추출 - @RequestParma 생략 가능")
    @Test
    void resolveArgument_required_false() {
        RequestParamMethodArgumentResolver resolver =
                RequestParamMethodArgumentResolver.of(false, SimpleTypeConverter.getTypeConverter());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "1");

        assertAll(
                () -> assertThat(resolver.supportsParameter(paramWithOutRequestParamMethodParam())).isTrue(),
                () -> assertThat(resolver.resolveArgument
                        (paramWithOutRequestParamMethodParam(), request, new MockHttpServletResponse()))
                        .isEqualTo(1L)
        );
    }

    @DisplayName("required = true이면 @RequestParam 생략 불가")
    @Test
    void resolveArgument_required_false_fail() throws NoSuchMethodException {
        RequestParamMethodArgumentResolver resolver =
                RequestParamMethodArgumentResolver.of(true, SimpleTypeConverter.getTypeConverter());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "1");

        assertThat(resolver.supportsParameter(paramWithOutRequestParamMethodParam())).isFalse();
    }

    private MethodParameter paramWithOutRequestParamMethodParam() throws NoSuchMethodException {
        Method method = RequestParamMethodArgumentResolverTest.class
                .getDeclaredMethod("paramWithOutRequestParam", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private MethodParameter paramWithRequestParamMethodParam() throws NoSuchMethodException {
        Method method = RequestParamMethodArgumentResolverTest.class
                .getDeclaredMethod("paramWithRequestParam", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private ModelAndView paramWithRequestParam(@RequestParam long userId) {
        return new ModelAndView(new MockView());
    }

    private ModelAndView paramWithOutRequestParam(long userId) {
        return new ModelAndView(new MockView());
    }
}
