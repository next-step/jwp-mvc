package core.mvc.tobe.scanner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.ReflectionUtils;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;

public class RequestMappingScanner {

	private static final int EMPTY_METHOD_LENGTH = 0;

	public RequestMappingScanner() {
	}

	public Map<HandlerKey, HandlerExecution> getHandlerExecutions(String path, Class<?> clazz, Object instance) {
		Map<HandlerKey, HandlerExecution> result = new HashMap<>();

		Set<Method> allMethods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
		allMethods.forEach(method -> result.putAll(addHandlerExecution(path, instance, method)));

		return result;
	}

	private Map<HandlerKey, HandlerExecution> addHandlerExecution(String path, Object handler, Method method) {
		Map<HandlerKey, HandlerExecution> result = new HashMap<>();

		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		RequestMethod[] methods = requestMapping.method();

		if (methods.length == EMPTY_METHOD_LENGTH) {
			methods = RequestMethod.values();
		}

		for (RequestMethod requestMethod : methods) {
			result.put(new HandlerKey(path + requestMapping.value(), requestMethod), new HandlerExecution(handler, method));
		}

		return result;
	}
}
