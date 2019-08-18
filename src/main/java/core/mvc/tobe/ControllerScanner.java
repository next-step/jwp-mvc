package core.mvc.tobe;


import com.google.common.collect.Sets;
import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ControllerScanner {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    public static Set<Object> scan(final Object... packages) {
        final Reflections reflections = new Reflections(packages);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        final Set<Object> controllers = Sets.newHashSet();
        for (Class<?> controllerClass : controllerClasses) {
            try {
                controllers.add(controllerClass.newInstance());
            } catch (Exception ex) {
                logger.debug(ex.getMessage());
            }
        }
        return controllers;
    }
}
