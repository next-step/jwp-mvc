package next.reflection;

import com.google.common.collect.Sets;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.MyController;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        Field[] fileds = clazz.getDeclaredFields();
        for (int i = 0; i < fileds.length; i++) {
            logger.debug("Fileds {}", fileds[i].getName());
        }

        Constructor<?>[] con = clazz.getConstructors();
        for (int i = 0; i < con.length; i++) {
            logger.debug("Constructor Name {}", con[i].getName());

        }

        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            logger.debug("Method {}", methods[i].getName());
        }
    }

    @Test
    public void privateFieldAccess() {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        try {
            Field nameField = clazz.getDeclaredField("name");
            Field ageField = clazz.getDeclaredField("age");

            Student studentInstance = clazz.newInstance();

            nameField.setAccessible(true);
            nameField.set(studentInstance, "박기태");

            ageField.setAccessible(true);
            ageField.set(studentInstance, 31);

            assertThat(studentInstance.getName()).isEqualTo("박기태");
            assertThat(studentInstance.getAge()).isEqualTo(31);
        }catch (NoSuchFieldException e){

        }catch (IllegalAccessException e){

        }catch (InstantiationException e){

        }
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
            if(parameterTypes.length == 3){
                Question question = (Question) constructor.newInstance("a", "b", "c");
                assertNotNull(question);
            }

            if(parameterTypes.length == 6){
                Question question = (Question) constructor.newInstance(0L, "a", "b", "c", new Date(), 0);
                assertNotNull(question);
            }
        }
        return beans;
    }

    Reflections reflections;
    @Test
    public void AnnotationSearch(){
        reflections = new Reflections("core.di.factory.example");
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        preInstanticateClazz.stream()
                .forEach(clazz -> {
                        logger.debug("Annotation Class{}", clazz);
                });

    }

    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

}
