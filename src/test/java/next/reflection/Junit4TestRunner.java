package next.reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for(Method method : declaredMethods) {
            invokeMethod(clazz, method);
        }
    }

    private void invokeMethod(Class<Junit4Test> clazz, Method method) throws Exception {
        if(isMyTestAnnotationPresent(method)) {
            method.invoke(clazz.newInstance());
        }
    }

    private boolean isMyTestAnnotationPresent(Method method) {
        return method.isAnnotationPresent(MyTest.class);
    }
}
