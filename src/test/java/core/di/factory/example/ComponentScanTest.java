package core.di.factory.example;

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

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        for (Class<?> controller : controllers) {
            logger.debug("## controller: {}", controller.getSimpleName());
        }

        for (Class<?> service : services) {
            logger.debug("## service: {}", service.getSimpleName());
        }

        for (Class<?> repository : repositories) {
            logger.debug("## repository: {}", repository.getSimpleName());
        }
    }
}
