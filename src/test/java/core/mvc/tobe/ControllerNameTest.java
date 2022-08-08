package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import next.fixture.controller.AnnotatedController;
import next.fixture.controller.NamedAnnotatedController;
import next.fixture.controller.NotAnnotatedController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerNameTest {

    @DisplayName("컨트롤러에 적용된 @Controller 애너테이션의 값으로 이름을 반환한다.")
    @Test
    void generate_handler_name_by_annotation_value() {
        final Class<?> namedController = NamedAnnotatedController.class;

        final String actual = ControllerName.generate(namedController);

        assertThat(actual).isEqualTo("namedController");
    }

    @DisplayName("컨트롤러에 적용된 @Controller 애너테이션의 값이 없으면 컨트롤러명을 lowerCamelCase 로 반환한다.")
    @Test
    void generate_lower_camel_case_handler_name_without_annotation_value() {
        final Class<?> annotatedController = AnnotatedController.class;

        final String actual = ControllerName.generate(annotatedController);

        assertThat(actual).isEqualTo("annotatedController");
    }

    @DisplayName("컨트롤러에 적용된 Controller 애너테이션이 없으면 예외를 발생시킨다")
    @Test
    void throw_exception_without_controller_annotation() {
        final Class<?> notAnnotatedController = NotAnnotatedController.class;

        assertThatThrownBy(() -> ControllerName.generate(notAnnotatedController))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Controller 애너테이션이 없습니다 : next.fixture.controller.NotAnnotatedController");

    }
}
