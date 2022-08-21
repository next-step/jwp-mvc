package core.mvc.tobe.scanner;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import core.annotation.web.Controller;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;
import core.mvc.tobe.support.ArgumentResolver;
import core.mvc.tobe.support.BeanTypeArgumentResolver;
import core.mvc.tobe.support.HttpRequestArgumentResolver;
import core.mvc.tobe.support.HttpResponseArgumentResolver;
import core.mvc.tobe.support.PathVariableArgumentResolver;
import core.mvc.tobe.support.RequestParamArgumentResolver;
import core.mvc.tobe.support.SimpleTypeArgumentResolver;

public class ControllerScanner {
	private final ControllerHandlers controllerHandlers;
	private final ControllerMethods controllerMethods;

	private static final List<ArgumentResolver> argumentResolvers = asList(new HttpRequestArgumentResolver(),
																		   new HttpResponseArgumentResolver(),
																		   new RequestParamArgumentResolver(),
																		   new SimpleTypeArgumentResolver(),
																		   new BeanTypeArgumentResolver(),
																		   new PathVariableArgumentResolver());

	public ControllerScanner(Object... basePackage) {
		Set<Class<?>> controllerWithAnnotation = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
		controllerHandlers = new ControllerHandlers(controllerWithAnnotation);
		controllerMethods = new ControllerMethods(controllerWithAnnotation);
	}

	public Map<HandlerKey, HandlerExecution> getHandlerExecutions() {
		return controllerMethods.getHandlerExecutions(controllerHandlers, argumentResolvers);
	}
}
