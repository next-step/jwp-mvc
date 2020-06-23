package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit4TestRunner.class);

    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        String annotationName = "@next.reflection.MyTest()";
        Method[] methods = clazz.getMethods();
        Arrays.stream(methods)
                .forEach(m -> {
                    logger.debug("name: {}", m.getName());

                    Annotation[] annotations = m.getAnnotations();
                    boolean hasMyTestAnnotation = Arrays.stream(annotations)
                            .filter(a -> annotationName.equals(a))
                            .findFirst().isPresent();

                    if (hasMyTestAnnotation) {
                        try {
                            m.invoke(clazz.newInstance());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                });
    }

    @Test
    @DisplayName("내부")
    public void run1() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Class<MyTest> annoClazz = MyTest.class;

        Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods)
                .forEach(m -> {
                    logger.debug("name: {}", m.getName());

                    Annotations annotations = new Annotations(m.getAnnotations());

                    if (annotations.hasAnnotation(annoClazz)) {
                        try {
                            m.invoke(clazz.newInstance());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                });
    }

    @Test
    public void run2() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods)
                .forEach(m -> {
                    logger.debug("name: {}", m.getName());

                    if (m.isAnnotationPresent(MyTest.class)) {
                        try {
                            m.invoke(clazz.newInstance());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
    }
}
