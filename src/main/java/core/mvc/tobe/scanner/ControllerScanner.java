package core.mvc.tobe.scanner;

import static java.util.Arrays.asList;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import core.annotation.web.Controller;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;
import core.mvc.tobe.scanner.exception.InstanceInitializedException;
import core.mvc.tobe.support.ArgumentResolver;
import core.mvc.tobe.support.BeanTypeArgumentResolver;
import core.mvc.tobe.support.HttpRequestArgumentResolver;
import core.mvc.tobe.support.HttpResponseArgumentResolver;
import core.mvc.tobe.support.PathVariableArgumentResolver;
import core.mvc.tobe.support.RequestParamArgumentResolver;
import core.mvc.tobe.support.SimpleTypeArgumentResolver;

public class ControllerScanner {
	private final Map<Class<?>, Object> controllers = new HashMap<>();
	private final ControllerMethods controllerMethods;
	private static final List<ArgumentResolver> argumentResolvers = asList(new HttpRequestArgumentResolver(),
																		   new HttpResponseArgumentResolver(),
																		   new RequestParamArgumentResolver(),
																		   new SimpleTypeArgumentResolver(),
																		   new BeanTypeArgumentResolver(),
																		   new PathVariableArgumentResolver());

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
			result.putAll(controllerMethods.getHandlerExecutions(argumentResolvers, clazz, instance, path));
		}

		return result;
	}
}
