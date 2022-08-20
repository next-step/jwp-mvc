package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {
	private final Object[] basePackages;

	private Map<HandlerKey, HandlerExecution> handlerExecutions;

	public AnnotationHandlerMapping(Object... basePackages) {
		this.basePackages = basePackages;
	}

	public void initialize() {
		ControllerScanner controllerScanner = new ControllerScanner(basePackages);
		Map<Class<?>, Object> controllers = controllerScanner.getControllers();

		handlerExecutions = controllers.keySet()
			.stream()
			.flatMap(clazz -> getRequestMappingMethod(clazz).stream())
			.collect(Collectors.toMap(
				HandlerKey::of,
				method -> new HandlerExecution(controllers.get(method.getDeclaringClass()), method))
			);
	}

	@SuppressWarnings("unchecked")
	private static Set<Method> getRequestMappingMethod(Class<?> clazz) {
		return ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
	}

	public HandlerExecution getHandler(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
		return getHandlerExecution(requestUri, requestMethod);
	}

	private HandlerExecution getHandlerExecution(String requestUri, RequestMethod requestMethod) {
		final HandlerExecution handlerExecution = handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
		if (handlerExecution != null) {
			return handlerExecution;
		}
		return handlerExecutions.get(new HandlerKey(requestUri, RequestMethod.ALL));
	}
}
