package next.reflection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class Junit3TestRunner {

    @DisplayName("요구사항 2 - JUnit3Test 클래스의 메서드 중에서 'test'로 시작하는 메서드만 실행한다.")
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
            .filter(method -> method.getName().startsWith("test"))
            .collect(Collectors.toList());

        for (Method method : methods) {
            method.invoke(clazz.getDeclaredConstructor().newInstance());
        }

        assertThat(methods).hasSize(2);
    }
}
