package core.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.exception.InstanceInitializedException;

public class ControllerScanner {
	private final Set<Class<?>> controllers;

	public ControllerScanner(Object... basePackage) {
		this.controllers = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
	}

	public Set<Class<?>> getControllers() {
		return controllers;
	}
	public Set<Method> getMethods(Class<?> controller) {
		return ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
	}

	public Object getHandlerInstance(Class<?> controller) {
		try {
			return controller.getDeclaredConstructor().newInstance();
		} catch (InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
			throw new InstanceInitializedException();
		}
	}
}
