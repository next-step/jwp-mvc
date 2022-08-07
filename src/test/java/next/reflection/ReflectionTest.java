package next.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private static final String DEPTH_1 = "\t\t";
    private static final String DEPTH_2 = "\t\t\t\t";
    private static final String DEPTH_3 = "\t\t\t\t\t\t";

    @DisplayName("요구사항 4, private field 값 할당")
    @Test
    void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student student = clazz.getDeclaredConstructor().newInstance();

        printBefore(student);
        assertThat(student.getName()).isNull();
        assertThat(student.getAge()).isZero();

        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");
        nameField.setAccessible(true);
        ageField.setAccessible(true);
        nameField.set(student, "수정된이름");
        ageField.set(student, 99);

        printAfter(student);
        assertThat(student.getName()).isEqualTo("수정된이름");
        assertThat(student.getAge()).isEqualTo(99);
    }

    private void printBefore(Student student) {
        logger.debug(" ======= Before ");
        logger.debug(student.toString());
    }

    private void printAfter(Student student) {
        logger.debug(" ======= After ");
        logger.debug(student.toString());
    }

    @DisplayName("요구사항 1, 클래스 정보 출력")
    @Test
    void showAboutClass() {
        Class<Question> clazz = Question.class;

        printFields(clazz);
        printConstructors(clazz);
        printMethods(clazz);
    }

    private void printFields(Class<Question> clazz) {
        logger.debug(" ========= Field ========= ");
        logger.debug(" DeclaredFields: ");
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            logger.debug(DEPTH_1 + declaredField);
            Annotation[] declaredAnnotations = declaredField.getDeclaredAnnotations();
            logger.debug(DEPTH_2 + " Field Annotations: ");
            for (Annotation declaredAnnotation : declaredAnnotations) {
                logger.debug(DEPTH_2 + declaredAnnotation);
            }
        }
        logger.debug("");
    }

    private void printMethods(Class<Question> clazz) {
        logger.debug(" ========= Method (Only declared) ========= ");
        Arrays.stream(clazz.getDeclaredMethods())
            .forEach(method -> {
                logger.debug("method: ");
                logger.debug(DEPTH_1 + method.getName());

                logger.debug(DEPTH_2 + "parameterTypes: ");
                Class<?>[] parameterTypes = method.getParameterTypes();
                for (Class<?> parameterType : parameterTypes) {
                    logger.debug(DEPTH_3 + parameterType);
                }

                logger.debug(DEPTH_2 + "annotations: ");
                Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                for (Annotation declaredAnnotation : declaredAnnotations) {
                    logger.debug(DEPTH_3 + declaredAnnotation);
                }
            });
    }

    private void printConstructors(Class<Question> clazz) {
        logger.debug(" ========= Constructor ========= ");
        Arrays.stream(clazz.getConstructors())
            .forEach(constructor -> {
                logger.debug("constructor: ");
                logger.debug(DEPTH_1 + constructor.getName());

                logger.debug(DEPTH_2 + "parameterTypes: ");
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                for (Class<?> parameterType : parameterTypes) {
                    logger.debug(DEPTH_3 + parameterType + ", ");
                }

                logger.debug(DEPTH_2 + "annotations: ");
                Annotation[] declaredAnnotations = constructor.getDeclaredAnnotations();
                for (Annotation declaredAnnotation : declaredAnnotations) {
                    logger.debug(DEPTH_2 + declaredAnnotation);
                }
            });
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
        }
    }
}
