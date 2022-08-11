package core.mvc.tobe.method;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.di.factory.example.MockView;
import core.mvc.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메소드 파라미터")
class MethodParameterTest {

    @Test
    @DisplayName("메소드와 파라미터로 생성")
    void instance() {
        assertThatNoException().isThrownBy(this::singleLongUserIdMethodParameter);
    }

    @Test
    @DisplayName("메소드와 파라미터는 필수")
    void instance_nullArguments_thrownIllegalArgumentException() {
        assertAll(
                () -> assertThatIllegalArgumentException()
                        .isThrownBy(() -> MethodParameter.of(null, singleLongUserIdParameterMethodParameterWithRequestParamParameter())),
                () -> assertThatIllegalArgumentException()
                        .isThrownBy(() -> MethodParameter.of(singleLongUserIdParameterMethod(), null))
        );
    }

    @Test
    @DisplayName("메소드에는 파라미터가 포함되어야 함")
    void instance_notContainsParameter_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MethodParameter.of(singleLongUserIdParameterMethod(), anyParameter()));
    }

    @Test
    @DisplayName("파라미터명 조회")
    void name() throws NoSuchMethodException {
        assertThat(singleLongUserIdMethodParameter().name()).isEqualTo("userId");
    }

    @Test
    @DisplayName("파라미터 타입 조회")
    void type() throws NoSuchMethodException {
        assertThat(singleLongUserIdMethodParameter().type()).isEqualTo(Long.TYPE);
    }

    @Test
    @DisplayName("파라미터에 PathVariable 애너테이션 포함 여부")
    void hasParameterAnnotation() throws NoSuchMethodException {
        assertThat(singleLongUserIdMethodParameter().hasParameterAnnotation(RequestParam.class)).isTrue();
    }

    @Test
    @DisplayName("파라미터에 PathVariable 애너테이션 정보 조회")
    void parameterAnnotation() throws NoSuchMethodException {
        //given, when
        RequestParam requestParam = singleLongUserIdMethodParameter().parameterAnnotation(RequestParam.class);
        //then
        assertThat(requestParam.required()).isTrue();
    }

    @Test
    @DisplayName("메소드에 RequestMapping 애너테이션 포함 여부")
    void hasMethodAnnotation() throws NoSuchMethodException {
        assertThat(singleLongUserIdMethodParameter().hasMethodAnnotation(RequestMapping.class)).isTrue();
    }

    @Test
    @DisplayName("메소드에 RequestMapping 정보 조회")
    void methodAnnotation() throws NoSuchMethodException {
        //given, when
        RequestMapping requestMapping = singleLongUserIdMethodParameter().methodAnnotation(RequestMapping.class);
        //then
        assertThat(requestMapping.method()).isEqualTo(new RequestMethod[]{RequestMethod.POST});
    }

    @Test
    @DisplayName("파라미터에 애너테이션 존재 여부")
    void doesNotHaveAnnotations() throws NoSuchMethodException {
        assertThat(singleLongUserIdMethodParameter().doesNotHaveAnnotations()).isFalse();
    }

    private MethodParameter singleLongUserIdMethodParameter() throws NoSuchMethodException {
        return MethodParameter.of(singleLongUserIdParameterMethod(),
                singleLongUserIdParameterMethodParameterWithRequestParamParameter());
    }

    private Method singleLongUserIdParameterMethod() throws NoSuchMethodException {
        return MethodParameterTest.class.getDeclaredMethod("singleLongUserIdParameter", Long.TYPE);
    }

    private Parameter singleLongUserIdParameterMethodParameterWithRequestParamParameter() throws NoSuchMethodException {
        return singleLongUserIdParameterMethod().getParameters()[0];
    }

    private Parameter anyParameter() throws NoSuchMethodException {
        return MethodParameterTest.class.getDeclaredMethod("anyMethod", String.class)
                .getParameters()[0];
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    private ModelAndView singleLongUserIdParameter(@RequestParam long userId) {
        return ModelAndView.of(Map.of("userId", userId), new MockView());
    }

    private ModelAndView anyMethod(String anyArgument) {
        return ModelAndView.from(new MockView());
    }
}
