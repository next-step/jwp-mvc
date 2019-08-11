package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        final List<Method> targetMethods = filterMethodsAnnotated(clazz, MyTest.class);
        Junit4Test junit4Test = clazz.newInstance();

        for(Method method : targetMethods) {
            method.invoke(junit4Test);
        }
    }

    private List<Method> filterMethodsAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
        final Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(m -> m.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }
}
