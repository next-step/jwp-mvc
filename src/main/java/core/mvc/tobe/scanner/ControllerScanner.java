package core.mvc.tobe.scanner;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import core.annotation.web.Controller;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;
import core.mvc.tobe.scanner.exception.InstanceInitializedException;

public class ControllerScanner {
	private final Map<Class<?>, Object> controllers = new HashMap<>();
	private final ControllerMethods controllerMethods;

	public ControllerScanner(Object... basePackage) {
		Set<Class<?>> controllerWithAnnotation = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
		controllerWithAnnotation.forEach(controller -> controllers.put(controller, createInstance(controller)));
		controllerMethods = new ControllerMethods(controllerWithAnnotation);
	}

	private String getControllerUriPath(Class<?> controller) {
		return controller.getAnnotation(Controller.class).value();
	}

	private Object createInstance(Class<?> controller) {
		try {
			return controller.getDeclaredConstructor().newInstance();
		} catch (InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
			throw new InstanceInitializedException();
		}
	}

	public Map<HandlerKey, HandlerExecution> getHandlerExecutions() {
		Map<HandlerKey, HandlerExecution> result = new HashMap<>();

		for (Class<?> clazz : controllers.keySet()) {
			Object instance = controllers.get(clazz);
			String path = getControllerUriPath(clazz);
			result.putAll(controllerMethods.getHandlerExecutions(clazz, instance, path));
		}

		return result;
	}
}
