package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.annotation.web.RequestMethod;
import core.mvc.tobe.exception.UnSupportedControllerInstanceException;
import java.util.Map;
import next.controller.AnnotatedController;
import next.controller.NotAnnotatedController;
import next.controller.WithoutMethodController;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMappingScannerTest {

    @DisplayName("@Controller 애너테이션이 적용되어 있지 않은 클래스는 예외를 발생시킨다")
    @Test
    void exception_without_controller_annotation() {
        final NotAnnotatedController notAnnotatedController = new NotAnnotatedController();

        final ThrowingCallable actual = () -> RequestMappingScanner.getHandlerExecutable(notAnnotatedController);

        assertThatThrownBy(actual).isInstanceOf(UnSupportedControllerInstanceException.class)
            .hasMessage("컨트롤러 애너테이션이 없는 클래스는 지원하지 않습니다: next.controller.NotAnnotatedController");
    }


    @DisplayName("@RequestMapping 애노테이션이 적용된 메서드가 없으면 빈 값을 반환한다")
    @Test
    void return_an_empty_value_if_not_method_with_request_mapping() {
        final WithoutMethodController withoutMethodController = new WithoutMethodController();

        final Map<HandlerKey, HandlerExecutable> actual = RequestMappingScanner.getHandlerExecutable(withoutMethodController);

        assertThat(actual).isEmpty();
    }

    @DisplayName("@RequestMapping 애노테이션이 적용된 메서드와 컨트롤러 정보로 HandlerKey, HandlerExecutable 쌍을 반환한다")
    @Test
    void returns_the_handler_key_and_Handler_executable_pair() throws NoSuchMethodException {
        // given
        final AnnotatedController annotatedController = new AnnotatedController();

        // when
        final Map<HandlerKey, HandlerExecutable> actual = RequestMappingScanner.getHandlerExecutable(annotatedController);

        // then
        final Map<HandlerKey, HandlerExecutable> expected = Map.of(
            new HandlerKey("/home", RequestMethod.GET),
            new HandlerExecution(annotatedController, AnnotatedController.class.getMethod("home")));

        assertThat(actual).isEqualTo(expected);
    }

}
