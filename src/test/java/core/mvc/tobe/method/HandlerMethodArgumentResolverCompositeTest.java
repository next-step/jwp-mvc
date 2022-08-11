package core.mvc.tobe.method;

import core.di.factory.example.MockView;
import core.mvc.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("메서드 인자 가져오기 Composite")
class HandlerMethodArgumentResolverCompositeTest {

    @Test
    @DisplayName("싱글톤 객체")
    void instance() {
        assertThatNoException()
                .isThrownBy(HandlerMethodArgumentResolverComposite::instance);
    }

    @Test
    @DisplayName("신규 생성 불가")
    void newInstance_thrownAssertionError() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> ReflectionUtils.newInstance(HandlerMethodArgumentResolverComposite.class));
    }

    @Test
    @DisplayName("long 타입 파라미터 지원")
    void supportsParameter() throws NoSuchMethodException {
        assertThat(HandlerMethodArgumentResolverComposite.instance()
                .supportsParameter(singleLongUserIdParameterMethod())).isTrue();
    }

    @Test
    @DisplayName("ModelAttribute 애노테이션 파라미터는 지원 안함")
    void supportsParameter_modelAttributeParameter() throws NoSuchMethodException {
        assertThat(HandlerMethodArgumentResolverComposite.instance()
                .supportsParameter(singleLongParameterWithModelAttributeMethod())).isFalse();
    }

    @Test
    @DisplayName("ModelAttribute 애노테이션 파라미터는 지원 안함")
    void resolveArgument() throws NoSuchMethodException {
        //given
        long oneId = 1L;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", String.valueOf(oneId));
        //when, then
        assertThat(HandlerMethodArgumentResolverComposite.instance()
                .resolveArgument(singleLongUserIdParameterMethod(), request, new MockHttpServletResponse()))
                .isEqualTo(oneId);
    }

    private MethodParameter singleLongUserIdParameterMethod() throws NoSuchMethodException {
        Method method = HandlerMethodArgumentResolverCompositeTest.class
                .getDeclaredMethod("singleLongUserIdParameter", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private MethodParameter singleLongParameterWithModelAttributeMethod() throws NoSuchMethodException {
        Method method = HandlerMethodArgumentResolverCompositeTest.class
                .getDeclaredMethod("singleLongParameterWithModelAttribute", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private ModelAndView singleLongUserIdParameter(long userId) {
        return ModelAndView.of(Map.of("userId", userId), new MockView());
    }

    private ModelAndView singleLongParameterWithModelAttribute(@ModelAttribute long userId) {
        return ModelAndView.of(Map.of("userId", userId), new MockView());
    }
}
