package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class Junit4TestRunner {
    @DisplayName("@Test 애노테이션 메소드 실행")
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEachOrdered(method -> {
                    try {
                        method.invoke(clazz.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
