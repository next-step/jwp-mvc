package core.di.factory;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.mvc.tobe.ReflectionUtil;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("특정 어노테이션을 가지고 있는 클래스를 출력하는 테스트")
    public void printAllAnnotatedClasses() {
        reflections = new Reflections("core.di.factory.example");

        Class<? extends Annotation>[] targetClasses = new Class[]{Controller.class, Service.class, Repository.class};
        Set<Class<?>> annotatedClasses = ReflectionUtil.getTypesAnnotatedWith(reflections, targetClasses);

        for (Class<?> clazz : annotatedClasses) {
            assertThat(containsAnyTargetAnnotation(clazz, targetClasses)).isTrue();
            log.debug("className: {}", clazz.getName());
        }
    }

    private boolean containsAnyTargetAnnotation(Class<?> clazz, Class<? extends Annotation>[] targetAnnotationClasses) {
        return Arrays.stream(targetAnnotationClasses)
                .anyMatch(clazz::isAnnotationPresent);
    }
}
