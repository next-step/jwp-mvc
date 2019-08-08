package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        List<Method> methodList = Arrays.asList(clazz.getDeclaredMethods());
        for (Method method : methodList) {
            Annotation testAnnotation = method.getAnnotation(MyTest.class);
            newInstanceByAnnotation(clazz, method, testAnnotation);
        }

    }

    private void newInstanceByAnnotation(Class<Junit4Test> clazz, Method method, Annotation testAnnotation) throws Exception {
        if (Objects.nonNull(testAnnotation)) {
            method.invoke(clazz.getDeclaredConstructor().newInstance());
        }
    }
}
