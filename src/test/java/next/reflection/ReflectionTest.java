package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private static final String NAME_VALUE = "재성";
    private static final int AGE_VALUE = 3;

    private static final String AGE_FIELD_NAME = "age";
    private static final String NAME_FIELD_NAME = "name";

    private static final String AGE_GETTER_NAME = "getAge";
    private static final String NAME_GETTER_NAME = "getName";


    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        // 필드
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {logger.debug("Field : {}", field);});
        // 생성자
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(constructor -> {logger.debug("Constructor : {}", constructor);});
        // 메소드
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> {logger.debug("Method : {}", method);});
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {

        Map<Class<?>, Object> sampleTypeValues = new HashMap<>();
        sampleTypeValues.put(int.class, 1);
        sampleTypeValues.put(long.class, 2L);
        sampleTypeValues.put(String.class, "test");
        sampleTypeValues.put(Date.class, new Date());

        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();

            List<Object> params = Arrays.stream(parameterTypes)
                    .map(type -> sampleTypeValues.get(type))
                    .collect(Collectors.toList());

            Object newInstance = constructor.newInstance(params.toArray(new Object[params.size()]));
            logger.debug("newInstance : {}", newInstance);
            assertThat(newInstance).isNotNull();
        }
    }

    @Test
    public void privateFieldAccess() {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        try {
            Object targetInstance = clazz.newInstance();

            Field ageField = clazz.getDeclaredField(AGE_FIELD_NAME);
            ageField.setAccessible(true);
            ageField.set(targetInstance, AGE_VALUE);

            Field nameField = clazz.getDeclaredField(NAME_FIELD_NAME);
            nameField.setAccessible(true);
            nameField.set(targetInstance, NAME_VALUE);

            Method ageGetter = clazz.getDeclaredMethod(AGE_GETTER_NAME);
            Object age = ageGetter.invoke(targetInstance);

            Method nameGetter = clazz.getDeclaredMethod(NAME_GETTER_NAME);
            Object name = nameGetter.invoke(targetInstance);


            assertThat(age).isEqualTo(AGE_VALUE);
            assertThat(name).isEqualTo(NAME_VALUE);


        } catch (Exception e){
            logger.error("{}", e);
            assertThat(e).doesNotThrowAnyException();
        }
    }


    @Test
    public void componentScan() {

        final String basePackage = "core.di.factory.example";

        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(basePackage))
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
                .filterInputsBy(new FilterBuilder().includePackage(basePackage))
        );

        logger.debug("{}", reflections.getTypesAnnotatedWith(Controller.class));
        logger.debug("{}", reflections.getTypesAnnotatedWith(Service.class));
        logger.debug("{}", reflections.getTypesAnnotatedWith(Repository.class));
    }

}
