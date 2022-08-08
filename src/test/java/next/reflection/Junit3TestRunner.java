package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Junit3TestRunner {

    @DisplayName("Junit3Test 클래스중, test로 시작하는 메서드만 실행한다.")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Constructor<Junit3Test> declaredConstructor = clazz.getDeclaredConstructor();
        Junit3Test junit3Test = declaredConstructor.newInstance();

        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            String methodName = declaredMethod.getName();
            if (methodName.startsWith("test")) {
                declaredMethod.invoke(junit3Test);
            }
        }
    }
}
