package core.mvc.tobe.scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.ReflectionUtils;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;
import core.mvc.tobe.MyController;

public class ControllerScannerTest {

	private static final String BASE_PACKAGE = "core.mvc.tobe";

	@Test
	@DisplayName("HandlerExecutions 추출 테스트")
	public void getHandlerExecutions() {
		ControllerScanner controllerScanner = new ControllerScanner(BASE_PACKAGE);

		Map<HandlerKey, HandlerExecution> handlerExecutions = controllerScanner.getHandlerExecutions();
		Set<Method> allMethods = ReflectionUtils.getAllMethods(MyController.class, ReflectionUtils.withAnnotation(RequestMapping.class));

		for (Method method : allMethods) {
			RequestMapping annotation = method.getAnnotation(RequestMapping.class);
			RequestMethod[] requestMethods = annotation.method();
			for (RequestMethod requestMethod : requestMethods) {
				HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
				assertAll(() -> assertThat(handlerExecutions).containsKey(handlerKey),
						  () -> assertThat(handlerExecutions.get(handlerKey)).isNotNull());
			}
		}
	}
}
