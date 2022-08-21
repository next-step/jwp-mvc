package core.mvc.tobe;

import org.reflections.Reflections;

import core.annotation.web.Controller;

public class ControllerScanner {

    private final AnnotationHandlerMapping annotationHandlerMapping;

    public ControllerScanner(Object... basePackage) {
        var reflections = new Reflections(basePackage);
        var targetClasses = reflections.getTypesAnnotatedWith(Controller.class);

        this.annotationHandlerMapping = new AnnotationHandlerMapping(targetClasses);
    }

    public AnnotationHandlerMapping getAnnotationHandlerMapping() {
        return annotationHandlerMapping;
    }
}
