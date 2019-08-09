package core.di.factory;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.stream.Stream;

public class FindComponentTest {

    private static final Logger logger = LoggerFactory.getLogger(FindComponentTest.class);

    @Test
    void findComponent() {
        Reflections reflections = new Reflections("core.di.factory.example");
        Stream.of(Repository.class, Service.class, Controller.class)
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Collection::stream)
                .map(Class::getName)
                .forEach(logger::info);
    }
}
