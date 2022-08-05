package core.di.factory;

import com.google.common.collect.Sets;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.di.factory.example.JdbcQuestionRepository;
import core.di.factory.example.JdbcUserRepository;
import core.di.factory.example.MyQnaService;
import core.di.factory.example.QnaController;
import core.mvc.tobe.MyController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BeanFactoryTest {

    private Reflections reflections;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        reflections = new Reflections("core.di.factory.example");
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        beanFactory = new BeanFactory(preInstanticateClazz);
        beanFactory.initialize();
    }

    @Test
    void di() throws Exception {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    @DisplayName("core.di.factory.example 패키지에서 @Controller, @Service, @Repository 애노테이션 설정된 클래스 찾기")
    void componentScanTest() {
        assertAll(
                () -> assertThat(reflections.getTypesAnnotatedWith(Controller.class))
                        .containsExactly(QnaController.class)
                        .doesNotContain(MyController.class),
                () -> assertThat(reflections.getTypesAnnotatedWith(Service.class))
                        .containsExactlyInAnyOrder(MyQnaService.class),
                () -> assertThat(reflections.getTypesAnnotatedWith(Repository.class))
                        .containsExactlyInAnyOrder(JdbcQuestionRepository.class, JdbcUserRepository.class)
        );
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
