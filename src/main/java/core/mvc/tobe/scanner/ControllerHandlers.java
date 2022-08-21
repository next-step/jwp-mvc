package core.mvc.tobe.scanner;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import core.mvc.tobe.scanner.exception.InstanceInitializedException;

public class ControllerHandlers {
	private final Set<ControllerHandler> controllerHandlers = new HashSet<>();

	public ControllerHandlers(Set<Class<?>> controllerWithAnnotation) {
		controllerWithAnnotation.forEach(controller -> controllerHandlers.add(new ControllerHandler(controller, createInstance(controller))));
	}

	private Object createInstance(Class<?> controller) {
		try {
			return controller.getDeclaredConstructor().newInstance();
		} catch (InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
			throw new InstanceInitializedException();
		}
	}

	public ControllerHandler getControllerHandler(Class<?> clazz) {
		return controllerHandlers.stream()
								 .filter(controllerHandler -> controllerHandler.getClazz().equals(clazz))
								 .findFirst()
								 .orElse(null);
	}

	public Object getControllerHandlerInstance(Class<?> clazz) {
		return controllerHandlers.stream()
								 .filter(controllerHandler -> controllerHandler.getClazz().equals(clazz))
								 .map(controllerHandler -> controllerHandler.getHandler())
								 .findFirst()
								 .orElse(null);
	}

	public Set<ControllerHandler> getControllerHandlers() {
		return controllerHandlers;
	}
}
