package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

public class Junit4TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit4TestRunner.class);

    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Stream.of(declaredMethods).forEach(method -> {
            Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            Stream.of(declaredAnnotations)
                    .filter(annotation -> annotation.annotationType().getSimpleName().equals("MyTest"))
                    .findFirst()
                    .ifPresent(e -> {
                        try {
                            method.invoke(clazz.newInstance());
                        } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                            ex.printStackTrace();
                        }
                    });
        });
    }

    @Test
    public void isAnnotationPresent() {
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Stream.of(declaredMethods).forEach(method -> {
            if (method.isAnnotationPresent(MyTest.class)) {
                try {
                    method.invoke(clazz.newInstance());
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            };
        });
    }
}
