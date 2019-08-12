package core.di.factory;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.di.factory.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ComponentScanTest {

    private Reflections reflections;
    private Set<Class> beanFactory;

    private static final Logger logger = LoggerFactory.getLogger(ComponentScanTest.class);

    @BeforeEach
    void setUp() {
        beanFactory = new HashSet<>();
        reflections = new Reflections("core.di.factory.example");
    }

    @Test
    void create_componentScan() {
        registerBeanFactory(Controller.class);
        registerBeanFactory(Service.class);
        registerBeanFactory(Repository.class);

        beanFactory.stream().forEach(clazz -> logger.debug("Register bean name : {} ", clazz.getName()));

        assertThat(beanFactory).containsOnly(
                QnaController.class,
                MyQnaService.class,
                JdbcUserRepository.class,
                JdbcQuestionRepository.class);
    }

    private void registerBeanFactory(Class<? extends Annotation> annotation) {
        Set<Class<?>> beans = reflections.getTypesAnnotatedWith(annotation);
        beanFactory.addAll(beans);
    }
}
