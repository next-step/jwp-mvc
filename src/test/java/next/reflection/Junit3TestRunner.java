package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Method[] declaredMethods = clazz.getDeclaredMethods();

        List<Method> methodStartWithTest = Stream.of(declaredMethods)
                .filter(method -> method.getName().startsWith("test"))
                .collect(Collectors.toList());

        methodStartWithTest.forEach(method -> {
            try {
                method.invoke(clazz.newInstance());
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        });

    }
}
