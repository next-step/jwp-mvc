package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            invokeIfMatches(method, clazz);
        }
    }

    private void invokeIfMatches(Method method, Class<Junit3Test> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if(method.getName().startsWith("test")) {
            method.invoke(clazz.newInstance());
        }
    }
}
