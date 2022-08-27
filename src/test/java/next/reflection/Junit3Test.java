package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit3Test {
    @Test
    public void runner() throws InvocationTargetException, IllegalAccessException {
        Class<Junit3Test> clazz = Junit3Test.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().startsWith("test")) {
                m.invoke(new Junit3Test());
            }
        }
    }

    public void test1() throws Exception {
        System.out.println("Running Test1");
    }

    public void test2() throws Exception {
        System.out.println("Running Test2");
    }

    public void three() throws Exception {
        System.out.println("Running Test3");
    }
}
