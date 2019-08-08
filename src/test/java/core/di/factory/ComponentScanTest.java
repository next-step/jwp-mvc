package core.di.factory;

import com.google.common.collect.Sets;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Created by youngjae.havi on 2019-08-08
 */
public class ComponentScanTest {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    @Test
    void component_scan() {
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        preInstanticateClazz.stream().forEach(c -> logger.debug(c.getName()));

    }

    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        String path = "core.di.factory.example";
        Reflections reflections = new Reflections(path);
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
