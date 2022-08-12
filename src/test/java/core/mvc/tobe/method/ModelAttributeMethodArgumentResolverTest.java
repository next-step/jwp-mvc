package core.mvc.tobe.method;

import core.di.factory.example.MockView;
import core.mvc.ModelAndView;
import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("객체 속성 메서드 인자 가져오기")
class ModelAttributeMethodArgumentResolverTest {

    public static final ModelAttributeMethodArgumentResolver TEST_RESOLVER = ModelAttributeMethodArgumentResolver.from(SimpleTypeConverter.instance());

    @Test
    @DisplayName("타입 컨버터로 생성")
    void instance() {
        assertThatNoException()
                .isThrownBy(() -> ModelAttributeMethodArgumentResolver.from(SimpleTypeConverter.instance()));
    }

    @Test
    @DisplayName("컨버터는 필수")
    void instance_null_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> ModelAttributeMethodArgumentResolver.from(null));
    }

    @Test
    @DisplayName("커스텀 타입 지원")
    void supportsParameter_customType() throws NoSuchMethodException {
        assertThat(TEST_RESOLVER.supportsParameter(customTypeParameterMethodParameter())).isTrue();
    }

    @Test
    @DisplayName("간단한 타입은 지원하지 않음")
    void supportsParameter_simpleType() throws NoSuchMethodException {
        assertThat(TEST_RESOLVER.supportsParameter(simpleTypeParameterMethodParameter())).isFalse();
    }

    @Test
    @DisplayName("요청 정보로 TestUser 생성")
    void resolveArgument() throws NoSuchMethodException {
        //given
        String oneId = "1";
        String password = "password";
        int tenAge = 10;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameters(Map.of("userId", oneId, "password", password, "age", String.valueOf(tenAge)));
        //when
        Object result = TEST_RESOLVER.resolveArgument(customTypeParameterMethodParameter(), request, new MockHttpServletResponse());
        //then
        assertThat(result).isEqualTo(new TestUser(oneId, password, tenAge));
    }

    private MethodParameter customTypeParameterMethodParameter() throws NoSuchMethodException {
        Method method = ModelAttributeMethodArgumentResolverTest.class
                .getDeclaredMethod("customTypeParameter", TestUser.class);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private MethodParameter simpleTypeParameterMethodParameter() throws NoSuchMethodException {
        Method method = ModelAttributeMethodArgumentResolverTest.class
                .getDeclaredMethod("simpleTypeParameter", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private ModelAndView customTypeParameter(TestUser testUser) {
        return ModelAndView.of(Map.of("user", testUser), new MockView());
    }

    private ModelAndView simpleTypeParameter(long userId) {
        return ModelAndView.of(Map.of("userId", userId), new MockView());
    }
}
