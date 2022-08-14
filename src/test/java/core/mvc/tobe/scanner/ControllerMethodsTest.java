package core.mvc.tobe.scanner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;

public class ControllerMethodsTest {
	private static final String BASE_PACKAGE = "core.mvc.tobe";

	@Test
	@DisplayName("메소드 추출 테스트")
	public void getMethods() {
		Set<Class<?>> controllerWithAnnotation = new Reflections(BASE_PACKAGE).getTypesAnnotatedWith(Controller.class);
		ControllerMethods controllerMethods = new ControllerMethods(controllerWithAnnotation);
		Class<?> myController = controllerWithAnnotation.stream()
														.filter(clazz -> clazz.getName().equals("core.mvc.tobe.MyController"))
														.findFirst()
														.orElse(null);

		Set<Method> actual = controllerMethods.getMethods(myController);
		Set<Method> expects = ReflectionUtils.getAllMethods(myController, ReflectionUtils.withAnnotation(RequestMapping.class));

		assertThat(actual).isEqualTo(expects);
	}
}
