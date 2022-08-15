package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            executeTest(clazz, method);
        }
    }

    private void executeTest(Class<Junit4Test> clazz, Method method) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Annotation[] annotations = method.getDeclaredAnnotations();
        if (hasMyTest(annotations)) {
            method.invoke(clazz.getDeclaredConstructor().newInstance());
        }
    }

    private boolean hasMyTest(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .anyMatch(annotation -> annotation.annotationType().equals(MyTest.class));
    }
}
