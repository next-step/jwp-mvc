package core.mvc.tobe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.web.Controller;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    private Reflections reflections;
    private Map<Class<?>, Object> map = Maps.newHashMap();

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
        instantiateControllers(getTypesAnnotatedWith(reflections, Controller.class));
    }

    private void instantiateControllers(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            try {
                map.put(clazz, clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }

    private Set<Class<?>> getTypesAnnotatedWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

    public Map<Class<?>, Object> getControllers() {
        return this.map;
    }

}
