package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {
    private final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    private Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> controllerScan() {
        Set<Class<?>> annotatedClazz = reflections.getTypesAnnotatedWith(Controller.class);

        return setControllerInstances(annotatedClazz);
    }

    private Map<Class<?>, Object> setControllerInstances(Set<Class<?>> annotatedClazz) {
        Map<Class<?>, Object> scanController = Maps.newHashMap();
        for(Class clazz : annotatedClazz) {
            setControllerInstance(scanController, clazz);
        }

        return scanController;
    }

    private void setControllerInstance(Map<Class<?>, Object> scanController, Class clazz) {
        try {
            scanController.put(clazz, clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            logger.debug("controller register fail");
        }
    }
}
