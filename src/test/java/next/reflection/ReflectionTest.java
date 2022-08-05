package next.reflection;

import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class ReflectionTest {
//    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private static final String DEPTH_1 = "\t";
    private static final String DEPTH_2 = "\t\t";
    private static final String DEPTH_3 = "\t\t\t";

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        // fields
        System.out.println(" ========= Field ========= ");
        System.out.println(" DeclaredFields ");
        Arrays.stream(clazz.getDeclaredFields())
            .map(Field::getName)
            .forEach(s -> {
                System.out.print(DEPTH_1 + s + ", ");
            });
        System.out.println("\n");

        System.out.println(" ========= Constructor ========= ");
        Arrays.stream(clazz.getConstructors())
                .forEach(constructor -> {
                    System.out.print("constructor: ");
                    System.out.println(DEPTH_1 + constructor.getName());
                    System.out.print(DEPTH_2 + "parameterTypes: ");
                    Arrays.stream(constructor.getParameterTypes())
                            .forEach(paramType -> {
                                System.out.print(paramType + ", ");
                            });
                    System.out.println();
                    System.out.println(DEPTH_2 + "annotations: ");
                    Arrays.stream(constructor.getDeclaredAnnotations())
                        .forEach(s -> {
                            System.out.println(DEPTH_2 + s);
                        });
                });
        System.out.println("");

        System.out.println(" ========= Method (Only declared) ========= ");
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> {
                    System.out.print("method: ");
                    System.out.println(DEPTH_1 + method.getName());
                    System.out.print(DEPTH_2 + "parameterTypes: ");
                    Arrays.stream(method.getParameterTypes())
                        .forEach(paramType -> {
                            System.out.print(paramType + ", ");
                        });
                    System.out.println();
                    System.out.println(DEPTH_2 + "annotations: ");
                    Arrays.stream(method.getDeclaredAnnotations())
                        .forEach(s -> {
                            System.out.println(DEPTH_2 + s);
                        });
                });
        System.out.println();
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
//            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
//                logger.debug("param type : {}", paramType);
            }
        }
    }
}
