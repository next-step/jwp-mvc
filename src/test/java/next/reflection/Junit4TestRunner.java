package next.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {
    @Test
    void run() {
        Class<Junit4Test> clazz = Junit4Test.class;
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .forEach(method -> {
                try {
                    method.invoke(clazz.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
