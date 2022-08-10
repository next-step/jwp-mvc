package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        clazz.getDeclaredMethods();

        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(MyTest.class)) {
                declaredMethod.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }

    }
}
