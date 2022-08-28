package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

class Junit3TestRunner {
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        Constructor<Junit3Test> constructor = clazz.getConstructor();

        Method[] methods = clazz.getMethods();
        Arrays.stream(methods).filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(constructor.newInstance());
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    }
                });
    }
}
