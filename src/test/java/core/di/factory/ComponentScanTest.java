package core.di.factory;

import com.google.common.collect.Sets;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.di.factory.example.MyQnaService;
import core.di.factory.example.QnaController;
import next.reflection.ReflectionTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ComponentScanTest {
    private static final Logger log = LoggerFactory.getLogger(ComponentScanTest.class);

    private Reflections reflections;

    @Test
    public void printAllAnnotatedClasses() {
        reflections = new Reflections("core.di.factory.example");

        Class[] targetClasses = {Controller.class, Service.class, Repository.class};
        Set<Class<?>> annotatedClasses = getTypesAnnotatedWith(targetClasses);

        for (Class<?> clazz : annotatedClasses) {
            assertThat(isAnnotationPresent(clazz, targetClasses)).isTrue();
            log.debug("className: {}", clazz.getName());
        }
    }

    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

    private boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation>... annotations) {
        return Arrays.stream(annotations)
            .anyMatch(clazz::isAnnotationPresent);
    }
}
