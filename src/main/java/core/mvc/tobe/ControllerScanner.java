package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by hspark on 2019-08-18.
 */
public class ControllerScanner {

    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    private Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }


    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return controllerClasses
                .stream()
                .map(it -> new AbstractMap.SimpleImmutableEntry<Class<?>, Object>(it, instantiate(it)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Object instantiate(Class<?> controllerClasse) {
        try {
            return controllerClasse.newInstance();
        } catch (Exception e) {
            throw new ControllerInstantiateFailedException(controllerClasse);
        }
    }


}
