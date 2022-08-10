package core.mvc.tobe.scanner;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import core.annotation.web.Controller;
import core.mvc.tobe.exception.InstanceInitializedException;

public class ControllerScanner {
	private final Map<Class<?>, Object> controllers = new HashMap<>();

	public ControllerScanner(Object... basePackage) {
		Set<Class<?>> controllerWithAnnotation = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
		controllerWithAnnotation.forEach(controller -> controllers.put(controller, createInstance(controller)));
	}

	public Map<Class<?>, Object> getControllers() {
		return controllers;
	}

	public String getControllerUriPath(Class<?> controller) {
		return controller.getAnnotation(Controller.class).value();
	}

	public Object createInstance(Class<?> controller) {
		try {
			return controller.getDeclaredConstructor().newInstance();
		} catch (InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
			throw new InstanceInitializedException();
		}
	}
}
