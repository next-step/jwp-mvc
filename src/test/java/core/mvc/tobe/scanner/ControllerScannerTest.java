package core.mvc.tobe.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.mvc.tobe.MyController;
import core.mvc.tobe.TestUserController;

public class ControllerScannerTest {

	private static final String BASE_PACKAGE = "core.mvc.tobe";
	private static final Class testClass = MyController.class;

	ControllerScanner controllerScanner = new ControllerScanner(BASE_PACKAGE);

	@Test
	@DisplayName("Controller 스캔 확인 테스트")
	public void controller_scan() {
		Map<Class<?>, Object> controllers = controllerScanner.getControllers();

		assertThat(controllers.keySet().contains(testClass)).isTrue();
	}

	@Test
	@DisplayName("Controller 인스턴스 생성 테스트")
	public void create_instance() {
		Map<Class<?>, Object> controllers = controllerScanner.getControllers();
		Object actual = controllers.get(testClass);
		MyController expect = new MyController();

		assertThat(actual.getClass()).isEqualTo(expect.getClass());
	}

	@Test
	@DisplayName("Controller URI 확인 테스트")
	public void controller_uri() {
		String controllerUriPath = controllerScanner.getControllerUriPath(TestUserController.class);

		assertThat(controllerUriPath).isEqualTo("/test");
	}
}
