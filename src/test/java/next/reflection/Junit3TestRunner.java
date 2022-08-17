package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class Junit3TestRunner {
    @Test
    public void run() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<Junit3Test> clazz = Junit3Test.class;

        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(clazz.newInstance());
            }
        }
    }
}
