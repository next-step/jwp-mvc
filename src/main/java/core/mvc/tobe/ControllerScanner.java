package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;


import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public class ControllerScanner {
    private static final Logger logger = getLogger(ControllerScanner.class);

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllers = Maps.newHashMap();

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> scan() {
        Set<Class<?>> controllerClasses = this.reflections.getTypesAnnotatedWith(Controller.class);
        save(controllerClasses);
        return controllerClasses;
    }

    private void save(Set<Class<?>> controllerClasses) {
        for (Class<?> clazz : controllerClasses) {
            try {
                controllers.put(clazz, clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("unable to load controller.", e);
            }
        }
    }

    public Set<Method> getMethodsWithRequestMapping(Class<?> clazz) {
        return ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    public Object get(Class<?> declaringClass) {
        return controllers.get(declaringClass);
    }
}
