package core.mvc.tobe;
import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    Map<Class<?>, Object> getControllerClassByInstance() {
        Map<Class<?>, Object> controllerClassByInstance = new HashMap();
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            Constructor<?> constructor = null;
            try {
                constructor = controller.getConstructor();
                controllerClassByInstance.put(controller, constructor.newInstance());
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return controllerClassByInstance;
    }

    Set<Class<?>> getControllerTypes() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
