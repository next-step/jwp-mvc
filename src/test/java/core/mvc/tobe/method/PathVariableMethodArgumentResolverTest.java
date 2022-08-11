package core.mvc.tobe.method;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.di.factory.example.MockView;
import core.mvc.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("경로 변수 메서드 인자 가져오기")
class PathVariableMethodArgumentResolverTest {

    public static final PathVariableMethodArgumentResolver TEST_RESOLVER = PathVariableMethodArgumentResolver.from(SimpleTypeConverter.instance());

    @Test
    @DisplayName("타입 컨버터로 생성")
    void instance() {
        assertThatNoException()
                .isThrownBy(() -> PathVariableMethodArgumentResolver.from(SimpleTypeConverter.instance()));
    }

    @Test
    @DisplayName("컨버터는 필수")
    void instance_null_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> PathVariableMethodArgumentResolver.from(null));
    }

    @Test
    @DisplayName("메소드에 RequestMapping, 인자에 PathVariable 가 존재해야 지원")
    void supportsParameter() throws NoSuchMethodException {
        assertThat(TEST_RESOLVER.supportsParameter(userIdParameterWithPathVariableMethodParameter())).isTrue();
    }

    @Test
    @DisplayName("인자에 PathVariable 없으면 지원안함")
    void supportsParameter_withoutPathVariable() throws NoSuchMethodException {
        assertThat(TEST_RESOLVER.supportsParameter(parameterWithoutPathVariableMethodParameter())).isFalse();
    }

    @Test
    @DisplayName("경로로 인자 가져오기")
    void resolveArgument() throws NoSuchMethodException {
        //given
        long oneId = 1;
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI(String.format("/users/%d", oneId));
        //when
        Object result = TEST_RESOLVER.resolveArgument(
                userIdParameterWithPathVariableMethodParameter(), servletRequest, new MockHttpServletResponse());
        //then
        assertThat(result).isEqualTo(oneId);
    }

    private MethodParameter userIdParameterWithPathVariableMethodParameter() throws NoSuchMethodException {
        Method method = PathVariableMethodArgumentResolverTest.class
                .getDeclaredMethod("userIdParameterWithPathVariable", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private MethodParameter parameterWithoutPathVariableMethodParameter() throws NoSuchMethodException {
        Method method = PathVariableMethodArgumentResolverTest.class.getDeclaredMethod("parameterWithoutPathVariable", Long.TYPE);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    private ModelAndView userIdParameterWithPathVariable(@PathVariable("userId") long id) {
        return ModelAndView.of(Map.of("userId", id), new MockView());
    }

    @RequestMapping
    private ModelAndView parameterWithoutPathVariable(long userId) {
        return ModelAndView.of(Map.of("userId", userId), new MockView());
    }
}
