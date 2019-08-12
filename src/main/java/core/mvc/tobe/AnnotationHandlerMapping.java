package core.mvc.tobe;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.exceptions.AnnotationHandlerMappingException;

public class AnnotationHandlerMapping {
	private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

	private Object[] basePackage;
	private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

	public AnnotationHandlerMapping(Object... basePackage) {
		this.basePackage = basePackage;
	}

	public void initialize() {
		try {
			basePackageInitialize(basePackage);
		} catch (AnnotationHandlerMappingException e) {
			logger.error("{}", e);
			throw e;
		}
	}

	public HandlerExecution getHandler(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
		return handlerExecutions.get(new HandlerKey(requestUri, rm));
	}

	private void basePackageInitialize(Object[] bases) {
		for (Object base : bases) {
			Reflections reflections = getBasePackageReflections(base);
			Map<HandlerKey, HandlerExecution> keyHandlerExecutions = getControllerClasses(reflections).stream()
					.flatMap(this::getHandlerExecutions)
					.collect(toMap(this::getHandlerKey, Function.identity(), (v1, v2) -> v1));
			this.handlerExecutions.putAll(keyHandlerExecutions);
		}
	}

	private HandlerKey getHandlerKey(HandlerExecution handlerExecution) {
		Method method = handlerExecution.getInvokeMethod();
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

		if (requestMapping == null) {
			throw new AnnotationHandlerMappingException("getHandlerKey fail by : " + RequestMapping.class);
		}

		return new HandlerKey(requestMapping.value(), requestMapping.method());
	}

	private Stream<HandlerExecution> getHandlerExecutions(Class<?> controllerClasses) {
		Object invokeInstance = getControllerNew(controllerClasses);

		return getRequestMappingMethod(controllerClasses)
				.stream()
				.map(invokeMethod -> HandlerExecution.of(invokeInstance, invokeMethod));
	}

	private Object getControllerNew(Class<?> controllerClasses) {
		try {
			return controllerClasses.newInstance();
		} catch (IllegalAccessException e) {
			throw new AnnotationHandlerMappingException(e);
		} catch (InstantiationException e) {
			throw new AnnotationHandlerMappingException(e);
		}
	}

	private Reflections getBasePackageReflections(Object basePackage) {
		return new Reflections(
				ConfigurationBuilder.build(basePackage)
				.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner())
				);
	}

	private Set<Class<?>> getClassesByAnnotation(Reflections reflections, Class<? extends Annotation> annotationClass) {
		return reflections.getTypesAnnotatedWith(annotationClass);
	}

	private Set<Class<?>> getControllerClasses(Reflections reflections) {
		return getClassesByAnnotation(reflections, Controller.class);
	}

	private Set<Method> getMethodByAnnotation(Class<?> controllerClass, Class<? extends Annotation> annotationClass) {
		return new Reflections(
				ConfigurationBuilder.build(controllerClass)
				.setScanners(new MethodAnnotationsScanner())
				).getMethodsAnnotatedWith(annotationClass);
	}

	private Set<Method> getRequestMappingMethod(Class<?> controllerClass) {
		return getMethodByAnnotation(controllerClass, RequestMapping.class);
	}
}
