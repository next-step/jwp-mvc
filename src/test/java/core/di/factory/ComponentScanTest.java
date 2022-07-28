package core.di.factory;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class ComponentScanTest {

    private static final Logger logger = LoggerFactory.getLogger(ComponentScanTest.class);
    private static final String PAKAGE = "core.di.factory.example";

    @Test
    @DisplayName("core.di.factory.example 패키지에서 @Controller 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.")
    void findComponent_controller() {
        Reflections reflections = new Reflections(PAKAGE, TypesAnnotated);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            logger.debug("className : {}", aClass.getName());
        }
    }

    @Test
    @DisplayName("core.di.factory.example 패키지에서 @Service애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.")
    void findComponent_service() {
        Reflections reflections = new Reflections(PAKAGE, TypesAnnotated);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            logger.debug("className : {}", aClass.getName());
        }
    }

    @Test
    @DisplayName("core.di.factory.example 패키지에서 @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.")
    void findComponent_repository() {
        Reflections reflections = new Reflections(PAKAGE, TypesAnnotated);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Repository.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            logger.debug("className : {}", aClass.getName());
        }
    }

}
