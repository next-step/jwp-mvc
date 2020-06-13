package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.di.factory.example.JdbcQuestionRepository;
import core.di.factory.example.JdbcUserRepository;
import core.di.factory.example.MyQnaService;
import core.di.factory.example.QnaController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ComponentScanTest {
    @DisplayName("Requirement - 6 : Component Scan")
    @Test
    void componentScan() {
        //given
        Reflections reflections = new Reflections("core.di.factory.example",
                new TypeAnnotationsScanner(),
                new SubTypesScanner());

        //when
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        //then
        assertThat(controllers).hasSize(1);
        assertThat(controllers.contains(QnaController.class)).isTrue();

        assertThat(services).hasSize(1);
        assertThat(services.contains(MyQnaService.class)).isTrue();

        assertThat(repositories).hasSize(2);
        assertThat(repositories.contains(JdbcQuestionRepository.class)).isTrue();
        assertThat(repositories.contains(JdbcUserRepository.class)).isTrue();
    }
}
