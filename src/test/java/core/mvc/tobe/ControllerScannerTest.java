package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ControllerScannerTest {

	private static final String BASE_PACKAGE = "core.mvc.tobe";
	private static final Class testClass = MyController.class;

	ControllerScanner controllerScanner = new ControllerScanner(BASE_PACKAGE);

	@Test
	@DisplayName("Controller 스캔 확인 테스트")
	public void controller_scan() {
		Set<Class<?>> controllers = controllerScanner.getControllers();

		assertThat(controllers.contains(testClass)).isTrue();
	}

	@Test
	@DisplayName("Controller 메소드 추출 테스트")
	public void extract_method() {
		Set<Method> actual = controllerScanner.getMethods(testClass);

		List<Method> expect = Arrays.stream(testClass.getDeclaredMethods()).collect(Collectors.toList());

		assertThat(expect).isEqualTo(actual.stream().collect(Collectors.toList()));
	}

	@Test
	@DisplayName("Controller 인스턴스 생성 테스트")
	public void create_instance() {
		Object actual = controllerScanner.getHandlerInstance(testClass);
		MyController expect = new MyController();

		assertThat(actual.getClass()).isEqualTo(expect.getClass());
	}
}
