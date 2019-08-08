package next.reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for(Method method : declaredMethods) {
            if(method.isAnnotationPresent(MyTest.class)) {
                method.invoke(clazz.newInstance());
            }
        }
    }
}
