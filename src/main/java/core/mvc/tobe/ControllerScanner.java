package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private Map<Class<?>, Object> annotationClassMapper;

    private ControllerScanner(Set<Class<?>> controllers) {
        annotationClassMapper = new HashMap<>();

        controllers.forEach(controller -> {
            try {
                annotationClassMapper.put(controller, controller.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static ControllerScanner of(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage
                , new TypeAnnotationsScanner()
                , new MethodAnnotationsScanner()
                , new SubTypesScanner());

        return new ControllerScanner(reflections.getTypesAnnotatedWith(Controller.class));
    }

    public Set<Class<?>> getClasses() {
        return Collections.unmodifiableSet(annotationClassMapper.keySet());
    }

    public Object getInstance(Class<?> controller) {
        return annotationClassMapper.get(controller);
    }
}
