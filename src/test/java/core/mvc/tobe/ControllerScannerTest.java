package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.tobe.exception.DuplicatedControllerDefinitionException;
import java.util.Set;
import next.controller.AnnotatedController;
import next.controller.NamedAnnotatedController;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class ControllerScannerTest {

    @DisplayName("@Controller 애노테이션이 적용된 클래스를 찾는다.")
    @Test
    void find_controllers_with_controller_annotation() {
        final Reflections reflections = new Reflections("next.controller");

        final ControllerScanner controllerScanner = new ControllerScanner(reflections);

        controllerScanner.instantiateControllers();

        final Set<Object> controllers = controllerScanner.getControllers();

        assertThat(controllers).hasOnlyElementsOfTypes(AnnotatedController.class, NamedAnnotatedController.class);
    }

    @DisplayName("@Controller 애노테이션이 적용된 클래스를 찾지 못한다.")
    @Test
    void cannot_find_controllers_with_controller_annotation() {
        final Reflections reflections = new Reflections("next.dao");

        final ControllerScanner controllerScanner = new ControllerScanner(reflections);

        controllerScanner.instantiateControllers();

        assertThat(controllerScanner.getControllers()).isEmpty();
    }

    @DisplayName("@Controller 애노테이션이 적용된 중복된 클래스를 찾는 경우 예외를 발생시킨다")
    @Test
    void cannot_find_duplicated_controllers_with_controller_annotation() {
        final Reflections reflections = new Reflections("next.duplicatedcontroller");

        final ControllerScanner controllerScanner = new ControllerScanner(reflections);

        final ThrowingCallable actual = controllerScanner::instantiateControllers;

        Assertions.assertThatThrownBy(actual)
            .isInstanceOf(DuplicatedControllerDefinitionException.class)
            .hasMessage("중복된 컨트롤러 인스턴스가 발견되었습니다: homeController");

    }

}
