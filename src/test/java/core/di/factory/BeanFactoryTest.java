package core.di.factory;

import com.google.common.collect.Sets;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.di.factory.example.JdbcQuestionRepository;
import core.di.factory.example.JdbcUserRepository;
import core.di.factory.example.MyQnaService;
import core.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    public void printClass() throws Exception {
        reflections = new Reflections("core.di.factory.example");
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        preInstanticateClazz.stream()
                .forEach(b -> {
                    logger.debug("name: {}", b.getName());
                });
    }


    @Test
    public void makeInstanceMyQnaService() throws Exception {
        Class<MyQnaService> clazz = MyQnaService.class;

        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);

        Constructor constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        Class[] parameterTypes = constructor.getParameterTypes();

        List<Object> objects = new ArrayList<>();
        for (Class param : parameterTypes) {
            try {
                Class conClass = BeanFactoryUtils.findConcreteClass(param, preInstanticateClazz);
                Constructor con = BeanFactoryUtils.getInjectedConstructor(conClass);
                if (con == null) {
                    objects.add(conClass.newInstance());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        logger.debug("objects: {}", objects);
        logger.debug("object: {}", constructor.newInstance(objects.toArray()));
    }

    @Test
    public void makeInstanceQnaController() throws Exception {
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);

        List<Object> objects1 = new ArrayList<>();
        Class<JdbcUserRepository> clazz1 = JdbcUserRepository.class;
        //--
        Object o1 = null;
        Constructor constructor1 = BeanFactoryUtils.getInjectedConstructor(clazz1);
        if (constructor1 == null) o1 = clazz1.newInstance();
        logger.debug("object: {}", o1);

        //-----------

        List<Object> objects2 = new ArrayList<>();
        Class<JdbcQuestionRepository> clazz2 = JdbcQuestionRepository.class;
        //--
        Object o2 = null;
        Constructor constructor2 = BeanFactoryUtils.getInjectedConstructor(clazz2);
        if (constructor2 == null) o2 = clazz2.newInstance();
        logger.debug("object: {}", o2);

        //-----------

        List<Object> objects3 = new ArrayList<>();
        Class<MyQnaService> clazz3 = MyQnaService.class;
        //--
        Object o3 = null;
        objects3.add(o1);
        objects3.add(o2);
        Constructor constructor3 = BeanFactoryUtils.getInjectedConstructor(clazz3);
        if (constructor3 == null) o3 = clazz3.newInstance();

        o3 = constructor3.newInstance(objects3.toArray());
        logger.debug("object: {}", o3);

        //-----------

        List<Object> objects4 = new ArrayList<>();
        Class<QnaController> clazz4 = QnaController.class;
        //--
        Object o4 = null;
        objects4.add(o3);
        Constructor constructor4 = BeanFactoryUtils.getInjectedConstructor(clazz4);
        if (constructor4 == null) o4 = clazz4.newInstance();
        logger.debug("objects: {}", objects4);
        o4 = constructor4.newInstance(objects4.toArray());
        logger.debug("object: {}", o4);
    }

    @Test
    public void makeInstanceQnaService2() throws Exception {
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);

        Class<MyQnaService> clazz3 = MyQnaService.class;
        //--
        Object o3 = BeanFactoryUtils.createInstance(clazz3, preInstanticateClazz);
        logger.debug("object: {}", o3);

    }

    @Test
    public void makeInstanceQnaController2() throws Exception {
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);

        List<Object> objects3 = new ArrayList<>();
        Class<QnaController> clazz3 = QnaController.class;
        //--
        Object o3 = BeanFactoryUtils.createInstance(clazz3, preInstanticateClazz);
        logger.debug("object: {}", o3);

    }
}
