package core.mvc.tobe;


import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class ControllerCollector {
    private static final Logger logger = LoggerFactory.getLogger(ControllerCollector.class);
    private static final Object EMPTY_CONTROLLER = new Object();

    public static Set<Object> collect(final Object... packages) {
        final Reflections reflections = new Reflections(packages);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return controllerClasses.stream()
                .map(ControllerCollector::getInstance)
                .filter(controller -> !controller.equals(EMPTY_CONTROLLER))
                .collect(Collectors.toSet());
    }

    private static Object getInstance(Class<?> controllerClass) {
        try {
            return controllerClass.newInstance();
        }
        catch (Exception ex) {
            logger.debug(ex.getMessage());
        }
        return EMPTY_CONTROLLER;
    }
}
