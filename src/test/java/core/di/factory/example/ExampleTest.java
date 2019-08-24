package core.di.factory.example;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import next.reflection.Junit4Test;
import next.reflection.ReflectionTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test  // 요구사항6
    public void findAnnotaionClass() {

        Class<?>[] classes = {Junit4Test.class, IntegrationConfig.class, JdbcQuestionRepository.class,
                JdbcUserRepository.class, MockView.class, MyJdbcTemplate.class, MyQnaService.class,
                QnaController.class, QuestionRepository.class, UserRepository.class};

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Controller.class) ||
                    clazz.isAnnotationPresent(Service.class) ||
                    clazz.isAnnotationPresent(Repository.class) ) {
                logger.debug("class name : {}", clazz.getName());
            }
        }


    }
}
