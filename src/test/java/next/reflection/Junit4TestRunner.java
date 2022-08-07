package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit4TestRunner extends AbstractTestRunner {

    @Test
    public void run() throws Exception {
        super.run(Junit4Test.class);
    }

    @Override
    public boolean isTestMethod(Method method) {
        return method.isAnnotationPresent(MyTest.class);
    }
}
