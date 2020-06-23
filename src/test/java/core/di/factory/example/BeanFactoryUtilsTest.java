package core.di.factory.example;

import com.google.common.collect.Sets;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.di.factory.BeanFactory;
import core.di.factory.BeanFactoryUtils;
import core.di.factory.PreInstanticateBeans;
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

public class BeanFactoryUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactoryUtilsTest.class);

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
    public void makeInstanceQnaService2() throws Exception {
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        PreInstanticateBeans preInstanticateBeans = new PreInstanticateBeans(preInstanticateClazz);
        Class<MyQnaService> clazz3 = MyQnaService.class;
        //--
        Object o3 = preInstanticateBeans.createInstance(clazz3);
        logger.debug("object: {}", o3);

    }

    @Test
    public void makeInstanceQnaController2() throws Exception {
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        PreInstanticateBeans preInstanticateBeans = new PreInstanticateBeans(preInstanticateClazz);
        Class<QnaController> clazz3 = QnaController.class;
        //--
        Object o3 = preInstanticateBeans.createInstance(clazz3);
        logger.debug("object: {}", o3);

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
