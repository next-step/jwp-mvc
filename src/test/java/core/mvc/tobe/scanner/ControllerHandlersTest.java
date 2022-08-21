package core.mvc.tobe.scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import core.annotation.web.Controller;
import core.mvc.tobe.MyController;
import core.mvc.tobe.TestUserController;

public class ControllerHandlersTest {
	private static final String BASE_PACKAGE = "core.mvc.tobe";

	@Test
	@DisplayName("클래스 별 Instance 생성 테스트")
	public void createInstance() {
		Set<Class<?>> controllerWithAnnotation = new Reflections(BASE_PACKAGE).getTypesAnnotatedWith(Controller.class);
		ControllerHandlers controllerHandlers = new ControllerHandlers(controllerWithAnnotation);

		assertAll(
				() -> assertThat(controllerHandlers.getControllerHandler(MyController.class)).isNotNull(),
				() -> assertThat(controllerHandlers.getControllerHandlerInstance(MyController.class)).isNotNull(),
				() -> assertThat(controllerHandlers.getControllerHandler(TestUserController.class)).isNotNull(),
				() -> assertThat(controllerHandlers.getControllerHandlerInstance(TestUserController.class)).isNotNull()
		);
	}
}
