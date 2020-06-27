package next.reflection;

import org.junit.jupiter.api.Test;


import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();
        Junit4Test junit4TestInstance = clazz.newInstance();
        Class<MyTest> myTestClass = MyTest.class;

        Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(myTestClass))
                .forEach(myTestMethod -> {
                    try {
                        myTestMethod.invoke(junit4TestInstance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

    }
}
