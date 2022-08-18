package core.mvc.tobe.scanner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.ReflectionUtils;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;
import core.mvc.tobe.support.ArgumentResolver;

public class ControllerMethods {

	private static final int EMPTY_METHOD_LENGTH = 0;

	private Map<Class<?>, Set<Method>> controllerMethods = new HashMap<>();

	public ControllerMethods(Set<Class<?>> controllerWithAnnotation) {
		controllerWithAnnotation.forEach(controller -> {
			Set<Method> methods = ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
			controllerMethods.put(controller, methods);
		});
	}

	public Map<HandlerKey, HandlerExecution> getHandlerExecutions(List<ArgumentResolver> argumentResolvers, Class<?> clazz, Object instance, String path) {
		Map<HandlerKey, HandlerExecution> result = new HashMap<>();

		Set<Method> methods = controllerMethods.get(clazz);
		methods.forEach(method -> result.putAll(getHandlerExecution(argumentResolvers, instance, method, path)));

		return result;
	}

	private Map<HandlerKey, HandlerExecution> getHandlerExecution(List<ArgumentResolver> argumentResolvers, Object handler, Method method, String path) {
		Map<HandlerKey, HandlerExecution> result = new HashMap<>();

		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		RequestMethod[] methods = requestMapping.method();

		if (methods.length == EMPTY_METHOD_LENGTH) {
			methods = RequestMethod.values();
		}

		for (RequestMethod requestMethod : methods) {
			result.put(new HandlerKey(path + requestMapping.value(), requestMethod), new HandlerExecution(argumentResolvers, handler, method));
		}

		return result;
	}

	public Set<Method> getMethods(Class<?> clazz) {
		return controllerMethods.get(clazz);
	}
}
