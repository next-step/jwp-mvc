package core.di.factory;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ComponentScanTest {
    private static final Logger logger = LoggerFactory.getLogger(ComponentScanTest.class);

    @Test
    void scan() {
        Reflections reflections = new Reflections("core.di.factory.example");

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            logger.debug("controller class name : {}", controllerClass.getName());
        }

        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);

        for (Class<?> serviceClass : serviceClasses) {
            logger.debug("service class name : {}", serviceClass.getName());
        }

        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        for (Class<?> repositoryClass : repositoryClasses) {
            logger.debug("repository class name : {}", repositoryClass.getName());
        }
    }
}
