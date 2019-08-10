package core.di.factory;

import core.annotation.Bean;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.di.factory.example.ExtendController;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindComponentTest {

    private static final Logger logger = LoggerFactory.getLogger(FindComponentTest.class);

    @Test
    void findComponent() {
        Reflections reflections = new Reflections("core.di.factory.example");
        List<String> expectedNames = asList(
                "JdbcQuestionRepository",
                "JdbcUserRepository",
                "MyQnaService",
                "ExtendController",
                "QnaController"
        );
        Stream.of(Repository.class, Service.class, Controller.class)
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Collection::stream)
                .map(Class::getSimpleName)
                .peek(name -> assertValidName(name, expectedNames))
                .forEach(logger::info);
    }

    private void assertValidName(String name, List<String> expectedNames) {
        assertTrue(expectedNames.contains(name));
    }

    @Test
    void reflections() {

        Reflections reflections = new Reflections("core.di.factory.example", new MethodAnnotationsScanner(), new SubTypesScanner(), new FieldAnnotationsScanner());
        Set<Constructor> constructorsAnnotatedWith = reflections.getConstructorsAnnotatedWith(Autowired.class);
        System.out.println(constructorsAnnotatedWith);

        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(Bean.class);
        System.out.println(methodsAnnotatedWith);

        Set<Field> fieldsAnnotatedWith = reflections.getFieldsAnnotatedWith(Value.class);
        System.out.println(fieldsAnnotatedWith);

        Set<Class<? extends ExtendController>> subTypesOf = reflections.getSubTypesOf(ExtendController.class);
        System.out.println(subTypesOf);

    }
}
