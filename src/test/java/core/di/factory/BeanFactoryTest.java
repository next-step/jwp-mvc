package core.di.factory;

import com.google.common.collect.Sets;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.di.factory.example.MyQnaService;
import core.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("BeanFactory 테스트")
public class BeanFactoryTest {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactoryTest.class);

    private Reflections reflections;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        reflections = new Reflections("core.di.factory.example");
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        beanFactory = new BeanFactory(preInstanticateClazz);
        beanFactory.initialize();
    }

    @Test
    public void di() throws Exception {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

    @DisplayName("core.di.factory.example 패키지에서 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.")
    @Test
    void componentScanTest() {
        Reflections reflections = new Reflections("core.di.factory.example");

        Set<Class<?>> typesAnnotatedWith = new HashSet<>();

        // 학습을 위해 위에서 제공하고 있는 getTypesAnnotatedWith( )메소드를 사용하지 않고 시도해봤습니다.
        Class<? extends Annotation>[] targetAnnotations = new Class[]{Controller.class, Service.class, Repository.class};

        Arrays.stream(targetAnnotations)
                .forEach(targetAnnotation -> {
                    typesAnnotatedWith.addAll(reflections.getTypesAnnotatedWith(targetAnnotation));
                });

        logger.debug("@Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스 : {}", typesAnnotatedWith);
    }
}
