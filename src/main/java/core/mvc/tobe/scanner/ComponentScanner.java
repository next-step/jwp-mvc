package core.mvc.tobe.scanner;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * @author : yusik
 * @date : 2019-08-10
 */
public class ComponentScanner {

    private static final Logger logger = LoggerFactory.getLogger(ComponentScanner.class);
    private static final Map<Class<?>, Object> beans = Maps.newHashMap();

    public static Map<Class<?>, Object> getControllers(String[] basePackages) {
        Set<Class<?>> controllers = scan(basePackages, Controller.class);
        for (Class controller : controllers) {
            Object bean = newInstance(controller);
            beans.put(controller, bean);
        }
        return beans;
    }

    private static Set<Class<?>> scan(String[] basePackages, Class<? extends Annotation>... annotations) {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> scannedAnnotations = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            scannedAnnotations.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return scannedAnnotations;
    }

    private static Object newInstance(Class<?> clazz) {
        Object instance = null;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("can't create bean: {}", clazz.getTypeName());
        }
        return instance;
    }
}
