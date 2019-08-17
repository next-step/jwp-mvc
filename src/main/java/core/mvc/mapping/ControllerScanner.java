package core.mvc.mapping;

import com.google.common.collect.ImmutableSet;
import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class);
        instantiateControllers(annotated);
    }

    private void instantiateControllers(Set<Class<?>> annotated) {
        annotated.forEach(controller -> {
            try {
                controllers.put(controller, controller.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Controller 인스턴스 생성중 오류가 발생하였습니다. class : [{}]",
                        controller.getName(), e);
            }
        });
    }

    public Set<Class<?>> getAllControllerClass() {
        return ImmutableSet.copyOf(controllers.keySet());
    }

    public Object getControllerInstance(Class<?> controllerClass) {
        return this.controllers.get(controllerClass);
    }
}
