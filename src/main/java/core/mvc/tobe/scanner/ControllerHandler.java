package core.mvc.tobe.scanner;

import core.annotation.web.Controller;

public class ControllerHandler {
	private final Class<?> clazz;
	private final Object instance;

	public ControllerHandler(Class<?> clazz, Object instance) {
		this.clazz = clazz;
		this.instance = instance;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public Object getHandler() {
		return instance;
	}

	public String getFullPath(String value) {
		return clazz.getAnnotation(Controller.class).value() + value;
	}
}
