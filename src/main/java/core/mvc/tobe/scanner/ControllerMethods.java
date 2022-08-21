package core.mvc.tobe.scanner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.ReflectionUtils;

import core.annotation.web.RequestMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;
import core.mvc.tobe.support.ArgumentResolver;

public class ControllerMethods {
	private Map<Class<?>, ControllerMethod> controllerMethods = new HashMap<>();

	public ControllerMethods(Set<Class<?>> controllerWithAnnotation) {
		controllerWithAnnotation.forEach(controller -> {
			Set<Method> methods = ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
			controllerMethods.put(controller, new ControllerMethod(methods));
		});
	}

	public Map<HandlerKey, HandlerExecution> getHandlerExecutions(ControllerHandlers controllerHandlers, List<ArgumentResolver> argumentResolvers) {
		Map<HandlerKey, HandlerExecution> result = new HashMap<>();

		for (ControllerHandler controllerHandler : controllerHandlers.getControllerHandlers()) {
			ControllerMethod controllerMethod = controllerMethods.get(controllerHandler.getClazz());
			result.putAll(controllerMethod.getHandlerExecutions(argumentResolvers, controllerHandler));
		}

		return 	result;
	}

	public Set<Method> getMethods(Class<?> clazz) {
		return controllerMethods.get(clazz).getMethods();
	}
}
