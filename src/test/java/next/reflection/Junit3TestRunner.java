package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

public class Junit3TestRunner {
    @DisplayName("test로 시작하는 메소드 실행")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Stream.of(clazz.getDeclaredMethods())
                .filter(method -> {
                    String methodName = method.getName();
                    return methodName.startsWith("test");
                })
                .forEach(method -> {
                    try {
                        method.invoke(clazz.newInstance());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }
}
