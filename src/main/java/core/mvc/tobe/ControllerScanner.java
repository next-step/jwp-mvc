package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        for (Class<?> annotatedController : reflections.getTypesAnnotatedWith(Controller.class)) {
            try {
                controllers.put(annotatedController, annotatedController.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Controller 생성에 실패 하였습니다. Error : " + e);
            }
        }
    }

    public Map<Class<?>, Object> controllers() {
        return Collections.unmodifiableMap(controllers);
    }

}
