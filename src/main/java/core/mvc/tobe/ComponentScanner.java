package core.mvc.tobe;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ComponentScanner {

    private ComponentScanner(){
    }

    public static Set<Class<?>> scan(Class<? extends Annotation> component, Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(component);
    }
}
