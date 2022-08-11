package core.mvc.tobe.method;

import core.annotation.web.RequestParam;
import core.di.factory.example.MockView;
import core.mvc.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@DisplayName("요청 파라미터 메소드 인자 가져오기")
class RequestParamMethodArgumentResolverTest {

    @Test
    @DisplayName("디폴트 설정 정보와 컨버터로 생성")
    void instance() {
        assertThatNoException()
                .isThrownBy(() -> RequestParamMethodArgumentResolver.of(true, SimpleTypeConverter.instance()));
    }

    @Test
    @DisplayName("컨버터는 필수")
    void instance_null_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> RequestParamMethodArgumentResolver.of(true, null));
    }

    @Test
    @DisplayName("RequestParam 애노테이션이 있으면 지원")
    void supportsParameter() throws NoSuchMethodException {
        //given
        RequestParamMethodArgumentResolver resolver = RequestParamMethodArgumentResolver.of(true, SimpleTypeConverter.instance());
        //when, then
        assertThat(resolver.supportsParameter(parameterWithRequestParamMethodParameter())).isTrue();
    }

    @Test
    @DisplayName("요청 타입으로 인자 가져오기")
    void resolveArgument() throws NoSuchMethodException {
        //given
        RequestParamMethodArgumentResolver resolver = RequestParamMethodArgumentResolver.of(true, SimpleTypeConverter.instance());

        long oneId = 1L;
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addParameter("userId", String.valueOf(oneId));
        //when, then
        assertThat(resolver.resolveArgument(parameterWithRequestParamMethodParameter(), servletRequest, new MockHttpServletResponse()))
                .isEqualTo(oneId);
    }

    private MethodParameter parameterWithRequestParamMethodParameter() throws NoSuchMethodException {
        Method method = RequestParamMethodArgumentResolverTest.class
                .getDeclaredMethod("parameterWithRequestParam", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private ModelAndView parameterWithRequestParam(@RequestParam long userId) {
        return ModelAndView.from(new MockView());
    }
}
