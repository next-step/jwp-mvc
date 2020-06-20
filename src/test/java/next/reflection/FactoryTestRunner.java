package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class FactoryTestRunner {
    private static final Logger logger = LoggerFactory.getLogger(FactoryTestRunner.class);

    private Reflections reflections;

    @BeforeEach
    void setUp() {
        reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage("core.di.factory.example"))
        );
    }

    @Test
    void show() {
        controller();
        service();
        repository();
    }

    @Test
    void controller() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        classes.forEach(clazz -> logger.debug("controller annotation class : {}", clazz.getName()));
    }

    @Test
    void service() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Service.class);
        classes.forEach(clazz -> logger.debug("service annotation class : {}", clazz.getName()));
    }

    @Test
    void repository() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Repository.class);
        classes.forEach(clazz -> logger.debug("repository annotation class : {}", clazz.getName()));
    }
}
