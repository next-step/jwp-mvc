package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if(methods[i].getName().contains("test")){
                methods[i].invoke(clazz.newInstance());
            }
        }
    }
}
