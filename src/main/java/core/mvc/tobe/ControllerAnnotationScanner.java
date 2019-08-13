package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ControllerAnnotationScanner {
    private static final Logger log = LoggerFactory.getLogger(ControllerAnnotationScanner.class);

    private final Reflections reflections;

    private Map<Class<?>, Object> instantiateControllers;

    private ControllerAnnotationScanner(Object... basePackages){
        this.reflections = new Reflections(
                ConfigurationBuilder.build(basePackages)
                        .setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner())
        );
    }

    public static ControllerAnnotationScanner getScanner(Object... basePackages) {
        return new ControllerAnnotationScanner(basePackages);
    }

    public Map<Class<?>, Object> getInstantiateControllers(){
        if(this.instantiateControllers == null) {
            setControllersInstance(findControllerClasses());
        }
        return this.instantiateControllers;
    }

    private Set<Class<?>> findControllerClasses() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void setControllersInstance(Set<Class<?>> controllerClasses) {
        this.instantiateControllers = controllerClasses.stream()
                .map( clazz -> {
                    try {
                      return clazz.newInstance();
                    } catch (IllegalAccessException e) {
                        log.error("{}", e);
                    } catch (InstantiationException e) {
                        log.error("{}", e);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap((instance -> instance.getClass()), Function.identity(), (v1, v2) -> v1))
                ;
    }


}
