package core.di.factory;

import com.google.common.collect.Sets;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ComponentScanTest {
    private static final Logger logger = LoggerFactory.getLogger(ComponentScanTest.class);

    @DisplayName("component scan")
    @Test
    void componentScan() {
        final Reflections reflections = new Reflections("core.di.factory.example");
        Set<Class<?>> classes = getTypesAnnotatedWith(reflections, Controller.class, Service.class, Repository.class);

        classes.forEach(clazz -> logger.debug(clazz.toString()));
    }

    @SafeVarargs
    private Set<Class<?>> getTypesAnnotatedWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
