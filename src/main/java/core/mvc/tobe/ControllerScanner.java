/*
package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

import static java.lang.reflect.Array.newInstance;

public class ControllerScanner {
    private final Map<Class<?>, Object> classInstances = Maps.newHashMap();

    public ControllerScanner(Object[] basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class, true);

        for (Class<?> controller : controllers) {
            classInstances.put(controller, newInstance(controller));
        }
    }


    public Set<Class<?>> getControllers() {
        return classInstances.keySet();
    }
}
*/
