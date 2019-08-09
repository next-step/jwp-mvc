package core.di.factory;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ComponentScanTest {

    private Reflections reflections;
    private static final Set<Class> BEAN_FACTORY = new HashSet<>();

    private static final Logger logger = LoggerFactory.getLogger(ComponentScanTest.class);

    @BeforeEach
    void setUp() {
        reflections = new Reflections("core.di.factory.example");
    }

    @Test
    void create_componentScan() {
        registerBeanFactory(Controller.class);
        registerBeanFactory(Service.class);
        registerBeanFactory(Repository.class);

        BEAN_FACTORY.stream().forEach(clazz -> logger.debug("bean name : {} ", clazz.getName()));
    }

    private void registerBeanFactory(Class<? extends Annotation> annotation) {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(annotation);
        BEAN_FACTORY.addAll(controllers);
    }
}
