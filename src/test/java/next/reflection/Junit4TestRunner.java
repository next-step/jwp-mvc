package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit4TestRunner {

    @DisplayName("Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행")
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();

        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(MyTest.class)) {
                declaredMethod.invoke(junit4Test);
            }
        }
    }
}
