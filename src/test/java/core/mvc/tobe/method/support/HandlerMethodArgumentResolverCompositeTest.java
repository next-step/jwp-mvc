package core.mvc.tobe.method.support;

import core.annotation.web.RequestParam;
import core.di.factory.example.MockView;
import core.mvc.ModelAndView;
import core.mvc.tobe.method.MethodParameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@DisplayName("메서드 인자 추출 Composite")
class HandlerMethodArgumentResolverCompositeTest {

    @DisplayName("Long 타입으로 형변환 지원")
    @Test
    void support_long() throws NoSuchMethodException {
        assertThat(HandlerMethodArgumentResolverComposite.getResolverComposite()
                .supportsParameter(singleLongUserIdParamMethod())).isTrue();
    }

    @DisplayName("@RequestParam 지원")
    @Test
    void resolveArgument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "1");

        assertThat(HandlerMethodArgumentResolverComposite.getResolverComposite()
                .resolveArgument(singleLongUserIdParamMethod(), request, new MockHttpServletResponse()))
                .isEqualTo(1L);
    }

    @DisplayName("@ModelAttribute는 단순 타입을 지원안함")
    @Test
    void support_param_modelAttribute() throws NoSuchMethodException {
        assertThat(HandlerMethodArgumentResolverComposite.getResolverComposite()
                .supportsParameter(longParamWithModelAttributeMethod())).isFalse();
    }

    private MethodParameter longParamWithModelAttributeMethod() throws NoSuchMethodException {
        Method method = HandlerMethodArgumentResolverCompositeTest.class
                .getDeclaredMethod("longParamWithModelAttribute", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private ModelAndView longParamWithModelAttribute(@ModelAttribute long userId) {
        ModelAndView modelAndView = new ModelAndView(new MockView());
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

    private MethodParameter singleLongUserIdParamMethod() throws NoSuchMethodException {
        Method method = HandlerMethodArgumentResolverCompositeTest.class.
                getDeclaredMethod("singleLongUserIdParam", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private ModelAndView singleLongUserIdParam(@RequestParam long userId) {
        ModelAndView modelAndView = new ModelAndView(new MockView());
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }
}
