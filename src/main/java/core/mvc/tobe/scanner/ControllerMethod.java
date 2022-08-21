package core.mvc.tobe.scanner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;
import core.mvc.tobe.support.ArgumentResolver;

public class ControllerMethod {
	private static final int EMPTY_METHOD_LENGTH = 0;

	private final Set<Method> methodSet;

	public ControllerMethod(Set<Method> methods) {
		this.methodSet = methods;
	}

	public Map<HandlerKey, HandlerExecution> getHandlerExecutions(List<ArgumentResolver> argumentResolvers, ControllerHandler controller) {
		Map<HandlerKey, HandlerExecution> result = new HashMap<>();
		methodSet.forEach(method -> result.putAll(getHandlerExecution(method, argumentResolvers, controller)));
		return result;
	}

	private Map<HandlerKey, HandlerExecution> getHandlerExecution(Method method, List<ArgumentResolver> argumentResolvers,
																  ControllerHandler controllerHandler) {
		Map<HandlerKey, HandlerExecution> result = new HashMap<>();

		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		RequestMethod[] methods = requestMapping.method();

		if (methods.length == EMPTY_METHOD_LENGTH) {
			methods = RequestMethod.values();
		}

		for (RequestMethod requestMethod : methods) {
			result.put(new HandlerKey(controllerHandler.getFullPath(requestMapping.value()), requestMethod),
					   new HandlerExecution(argumentResolvers, controllerHandler.getHandler(), method));
		}

		return result;
	}

	public Set<Method> getMethods() {
		return methodSet;
	}
}
