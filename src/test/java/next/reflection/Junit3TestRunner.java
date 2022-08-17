package next.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Junit3TestRunner {

    private static final String START_METHOD_NAME = "test";

    @DisplayName("JUnit3Test 클래스의 메서드 중에서 'test'로 시작하는 메서드만 실행한다.")
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Constructor<Junit3Test> constructor = clazz.getConstructor();

        List<Object> methods = Arrays.stream(clazz.getDeclaredMethods())
            .filter(method -> method.getName().startsWith(START_METHOD_NAME))
            .map(method -> {
                try {
                    return method.invoke(constructor.newInstance());
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            })
            .collect(Collectors.toList());

        assertThat(methods).hasSize(2);
    }
}
