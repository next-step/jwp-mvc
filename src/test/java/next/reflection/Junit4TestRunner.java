package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Constructor<?> constructor = clazz.getConstructor();
        Object junit4TestConstructor = constructor.newInstance();
        List<String> methodNames = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class)).map(Method::getName).collect(Collectors.toList());
        for (String methodName : methodNames) {
            Method declaredMethod = clazz.getDeclaredMethod(methodName);
            declaredMethod.invoke(junit4TestConstructor);
        }
    }
}
