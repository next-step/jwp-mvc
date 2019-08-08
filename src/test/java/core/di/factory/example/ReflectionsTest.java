package core.di.factory.example;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;


public class ReflectionsTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void componentScan() {
        Reflections reflections = new Reflections("core.di.factory.example", new TypeAnnotationsScanner(), new SubTypesScanner());

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            logger.debug("controller : {}", controllerClass.getName());
        }

        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> serviceClass : serviceClasses) {
            logger.debug("service : {}", serviceClass);
        }

        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        for (Class<?> repositoryClass : repositoryClasses) {
            logger.debug("repository : {}", repositoryClass);
        }

    }

}
