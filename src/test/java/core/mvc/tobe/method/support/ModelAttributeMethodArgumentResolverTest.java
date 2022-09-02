package core.mvc.tobe.method.support;

import core.mvc.ModelAndView;
import core.mvc.tobe.TestUser;
import core.mvc.tobe.method.MethodParameter;
import core.mvc.tobe.method.SimpleTypeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("@ModelAttribute 메서드 인자 추출")
class ModelAttributeMethodArgumentResolverTest {

    public static final ModelAttributeMethodArgumentResolver RESOLVER =
            new ModelAttributeMethodArgumentResolver(SimpleTypeConverter.getTypeConverter());

    @DisplayName("객체 생성")
    @Test
    void instance() {
        assertThatNoException().isThrownBy(
                () -> new ModelAttributeMethodArgumentResolver(SimpleTypeConverter.getTypeConverter()));
    }

    @DisplayName("객체 생성 실패")
    @Test
    void missing_type_converter() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new ModelAttributeMethodArgumentResolver(null));
    }

    @DisplayName("커스텀 타입 지원")
    @Test
    void support_param_custom_type() throws NoSuchMethodException {
        assertThat(RESOLVER.supportsParameter(customTypeParam())).isTrue();
    }

    @DisplayName("simple 타입 지원하지 않음")
    @Test
    void support_param_simple_type() throws NoSuchMethodException {
        assertThat(RESOLVER.supportsParameter(simpleTypeParam())).isFalse();
    }

    @DisplayName("Request 바탕으로 TestUser 생성")
    @Test
    void resolve_argument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameters(Map.of(
                "userId", "1",
                "password", "password",
                "age", "25"));

        Object result = RESOLVER.resolveArgument(customTypeParam(), request, new MockHttpServletResponse());

        assertThat(result).isEqualTo(new TestUser("1", "password", 25));
    }

    private MethodParameter simpleTypeParam() throws NoSuchMethodException {
        Method method = ModelAttributeMethodArgumentResolverTest.class
                .getDeclaredMethod("simpleTypeParameter", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private MethodParameter customTypeParam() throws NoSuchMethodException {
        Method method = ModelAttributeMethodArgumentResolverTest.class
                .getDeclaredMethod("customTypeParameter", TestUser.class);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private ModelAndView customTypeParameter(TestUser testUser) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", testUser);
        return modelAndView;
    }

    private ModelAndView simpleTypeParameter(long userId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }
}
