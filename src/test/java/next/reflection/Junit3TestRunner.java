package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit3TestRunner {
    private static final String PREFIX_METHOD_NAME = "test";

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Constructor<?> constructor = clazz.getConstructor();
        Object junit3TestConstructor = constructor.newInstance();
        List<String> methodNames = Arrays.stream(clazz.getDeclaredMethods()).map(Method::getName)
                .filter(name -> name.startsWith(PREFIX_METHOD_NAME)).collect(Collectors.toList());
        for (String methodName : methodNames) {
            Method declaredMethod = clazz.getDeclaredMethod(methodName);
            declaredMethod.invoke(junit3TestConstructor);
        }
    }
}
