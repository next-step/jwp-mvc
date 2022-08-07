package core.mvc.tobe;

import core.di.factory.example.MockView;
import core.mvc.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("핸들러 실행자")
class HandlerExecutionTest {

    private static final HandlerExecutionTest INVOKED_INSTANCE = new HandlerExecutionTest();

    @Test
    @DisplayName("요청 매핑 메소드로 생성")
    void instance() {
        assertAll(
                () -> assertThatNoException().isThrownBy(() -> new HandlerExecution(INVOKED_INSTANCE, requestMappingMethod())),
                () -> assertThatNoException().isThrownBy(() -> new HandlerExecution(INVOKED_INSTANCE, inversionParameterTypesRequestMappingMethod()))
        );
    }

    @Test
    @DisplayName("대상 객체와 메소드는 필수")
    void instance_null_thrownIllegalArgumentException() {
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new HandlerExecution(INVOKED_INSTANCE, null)),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new HandlerExecution(null, requestMappingMethod()))
        );
    }

    @Test
    @DisplayName("메소드의 파라미터 타입은 반드시 HttpServletRequest, HttpServletResponse")
    void instance_invalidParameterType_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException().isThrownBy(() -> new HandlerExecution(INVOKED_INSTANCE, objectTypeParameterMethod()));
    }

    @Test
    @DisplayName("메소드의 반환 타입은 반드시 ModelAndView")
    void instance_invalidReturnType_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException().isThrownBy(() -> new HandlerExecution(INVOKED_INSTANCE, objectReturnTypeMethod()));
    }

    @Test
    @DisplayName("요청 정보로 메소드 호출")
    void handle() throws Exception {
        //given
        HandlerExecution execution = new HandlerExecution(INVOKED_INSTANCE, requestMappingMethod());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        //when
        execution.handle(request, response);
        //then
        assertAll(
                () -> verify(request).getMethod(),
                () -> verify(response).getStatus()
        );
    }

    private Method requestMappingMethod() throws NoSuchMethodException {
        Method requestMappingMethod = HandlerExecutionTest.class.getDeclaredMethod("requestMappingMethod", HttpServletRequest.class, HttpServletResponse.class);
        requestMappingMethod.setAccessible(true);
        return requestMappingMethod;
    }

    private Method objectReturnTypeMethod() throws NoSuchMethodException {
        return HandlerExecutionTest.class.getDeclaredMethod("objectReturnTypeMethod", HttpServletRequest.class, HttpServletResponse.class);
    }

    private Method inversionParameterTypesRequestMappingMethod() throws NoSuchMethodException {
        return HandlerExecutionTest.class.getDeclaredMethod("requestMappingMethod", HttpServletResponse.class, HttpServletRequest.class);
    }

    private Method objectTypeParameterMethod() throws NoSuchMethodException {
        return HandlerExecutionTest.class.getDeclaredMethod("objectTypeParameterMethod", Object.class);
    }

    private ModelAndView objectTypeParameterMethod(Object object) {
        return ModelAndView.from(new MockView());
    }

    private Object objectReturnTypeMethod(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    private ModelAndView requestMappingMethod(HttpServletRequest request, HttpServletResponse response) {
        request.getMethod();
        response.getStatus();
        return ModelAndView.from(new MockView());
    }

    private ModelAndView requestMappingMethod(HttpServletResponse response, HttpServletRequest request) {
        return ModelAndView.from(new MockView());
    }
}
