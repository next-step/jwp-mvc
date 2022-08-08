package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.annotation.web.Controller;
import core.mvc.tobe.exception.DuplicatedControllerDefinitionException;
import java.util.Set;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class ControllerScanner2Test {

    private Reflections reflections;

    @DisplayName("@Controller 애노테이션이 적용된 클래스를 찾는다.")
    @Test
    void find_controllers_with_controller_annotation() {
        reflections = new Reflections("next.controller");

        final Set<Object> controllers = ControllerScanner2.getControllers(reflections);

        final Set<Class<?>> expected = reflections.getTypesAnnotatedWith(Controller.class);
        assertThat(controllers).hasOnlyElementsOfTypes(expected.toArray(new Class<?>[0]));
    }

    @DisplayName("@Controller 애노테이션이 적용된 클래스를 찾지 못한다.")
    @Test
    void cannot_find_controllers_with_controller_annotation() {
        reflections = new Reflections("next.dao");

        final Set<Object> controllers = ControllerScanner2.getControllers(reflections);

        assertThat(controllers).isEmpty();
    }

    @DisplayName("@Controller 애노테이션이 적용된 중복된 클래스를 찾는 경우 예외를 발생시킨다")
    @Test
    void cannot_find_duplicated_controllers_with_controller_annotation() {
        reflections = new Reflections("next.duplicatedcontroller");

        final ThrowingCallable actual = () -> ControllerScanner2.getControllers(reflections);

        assertThatThrownBy(actual)
            .isInstanceOf(DuplicatedControllerDefinitionException.class)
            .hasMessage("중복된 컨트롤러 인스턴스가 발견되었습니다: homeController");

    }
}
