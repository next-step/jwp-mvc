package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ComponentScanTest {

    private static final Logger log = LoggerFactory.getLogger(ComponentScanTest.class);

    @DisplayName("탐색할 어노테이션이 있는 친구들을 찾아보자 ㅇㅅㅇ")
    @Test
    void test_component_scan() {
        final Reflections reflections = new Reflections(
                ConfigurationBuilder
                        .build("core.di.factory.example")
                        .setScanners(new SubTypesScanner(false))
        );
        final Set<Class<? extends Object>> classSet = reflections.getSubTypesOf(Object.class);
        classSet.stream()
                .filter(this::isAnnotatedWithCoreAnnotations)
                .forEach(clazz -> log.debug("class name: {}", clazz));
    }

    private boolean isAnnotatedWithCoreAnnotations(Class<?> clazz) {
        return clazz.isAnnotationPresent(Controller.class) ||
                clazz.isAnnotationPresent(Service.class) ||
                clazz.isAnnotationPresent(Repository.class);
    }
}
