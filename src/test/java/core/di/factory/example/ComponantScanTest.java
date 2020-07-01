package core.di.factory.example;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponantScanTest {
    private static final Logger logger = LoggerFactory.getLogger(ComponantScanTest.class);
    private Reflections reflections;

    @Test
    void componentscan() {
        reflections = new Reflections("core.di.factory.example", ComponantScanTest.class);
//        reflections = new Reflections(ComponantScanTest.class);
        reflections.getTypesAnnotatedWith(Controller.class).stream()
                .forEach(s -> logger.debug("@Controller class name : " + s.getName()));
        reflections.getTypesAnnotatedWith(Service.class).stream()
                .forEach(s -> logger.debug("@Service class name : " + s.getName()));
        reflections.getTypesAnnotatedWith(Repository.class).stream()
                .forEach(s -> logger.debug("@Repository class name : " + s.getName()));
    }
}
