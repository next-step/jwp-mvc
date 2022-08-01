package core.di.factory;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.di.factory.example.QnaController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class ComponentScanTest {

    private static final Logger logger = LoggerFactory.getLogger(ComponentScanTest.class);
    private static final String PAKAGE = "core.di.factory.example";

    @ParameterizedTest
    @MethodSource("provideArgumentOfAnnotation")
    @DisplayName("core.di.factory.example 패키지에서 @Controller 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.")
    void findComponent_controller(Class<? extends Annotation> clazz, Class<? extends Annotation> actualClass) {
        Set<Class<?>> typesAnnotatedWith = getClasses(clazz);

        Assertions.assertThat(typesAnnotatedWith).contains(actualClass);
    }

    private Set<Class<?>> getClasses(Class<? extends Annotation> clazz) {
        Reflections reflections = new Reflections(PAKAGE, TypesAnnotated);
        return reflections.getTypesAnnotatedWith(clazz);
    }

    private static Stream<Arguments> provideArgumentOfAnnotation() {
        return Stream.of(
                Arguments.of(Controller.class, QnaController.class),
                Arguments.of(Service.class, "core.di.factory.example.MyQnaService"),
                Arguments.of(Repository.class, "core.di.factory.example.JdbcUserRepository"),
                Arguments.of(Repository.class, "core.di.factory.example.JdbcUserRepository")
        );
    }
}
