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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메서드 파라미터")
class MethodParameterTest {

    @DisplayName("객체 생성")
    @Test
    void instance() {
        assertThatNoException().isThrownBy(this::singleLongUserIdMethodParam);
    }

    @DisplayName("객체 생성 실패")
    @Test
    void instance_fail() {
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(
                        () -> MethodParameter.of(null, singleLongUserIdParamMethodParamWithRequestParam())),
                () -> assertThatIllegalArgumentException().isThrownBy(
                        () -> MethodParameter.of(singleLongUserIdParamMethod(), null))
        );
    }

    @DisplayName("인자 이름 조회")
    @Test
    void param_name() throws NoSuchMethodException {
        assertThat(singleLongUserIdMethodParam().getName()).isEqualTo("userId");
    }

    @DisplayName("인자 타입 조회")
    @Test
    void param_type() throws NoSuchMethodException {
        assertThat(singleLongUserIdMethodParam().getType()).isEqualTo(Long.TYPE);
    }

    @DisplayName("@RequestParam 설정된 인자 존재 여부")
    @Test
    void has_param_with_requestParam() throws NoSuchMethodException {
        assertThat(singleLongUserIdMethodParam().hasParameterAnnotation(RequestParam.class)).isTrue();
    }

    @DisplayName("메서드에 @RequestMapping 설정 여부")
    @Test
    void has_method_with_requestMapping() throws NoSuchMethodException {
        assertThat(singleLongUserIdMethodParam().hasMethodAnnotation(RequestMapping.class)).isTrue();
    }

    @DisplayName("메서드의 @RequestMapping 정보 조회")
    @Test
    void find_method_info() throws NoSuchMethodException {
        RequestMapping mapping = singleLongUserIdMethodParam().methodAnnotation(RequestMapping.class);

        assertThat(mapping.method()).isEqualTo(new RequestMethod[]{RequestMethod.POST}[0]);
    }

    private MethodParameter singleLongUserIdMethodParam() throws NoSuchMethodException {
        return MethodParameter.of(singleLongUserIdParamMethod(),
                singleLongUserIdParamMethodParamWithRequestParam());
    }

    private Parameter singleLongUserIdParamMethodParamWithRequestParam() throws NoSuchMethodException {
        return singleLongUserIdParamMethod().getParameters()[0];
    }

    private Method singleLongUserIdParamMethod() throws NoSuchMethodException {
        return MethodParameterTest.class.getDeclaredMethod("singleLongUserIdParam", Long.TYPE);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    private ModelAndView singleLongUserIdParam(@RequestParam long userId) {
        ModelAndView modelAndView = new ModelAndView(new MockView());
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }
}
