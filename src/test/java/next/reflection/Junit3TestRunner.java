package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit3TestRunner {

    @Test // 요구사항2
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        Constructor<?> c = clazz.getConstructor();
        Junit3Test instance = (Junit3Test) c.newInstance();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            invoke(method, instance);
        }
    }

    public void invoke(Method method, Object instance) throws InvocationTargetException, IllegalAccessException {
        if (method.getName().startsWith("test")) {
            method.invoke(instance);
        }
    }
}
