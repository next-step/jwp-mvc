package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method m : declaredMethods) {
            if (containsMyTestAnnotation(m)) {
                m.invoke(new Junit4Test());
            }
        }
    }

    public boolean containsMyTestAnnotation(Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        return Arrays.stream(annotations).anyMatch(a -> a.annotationType().getSimpleName().equals("MyTest"));
    }
}
