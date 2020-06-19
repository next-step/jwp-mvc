package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Junit3TestRunner {

    @DisplayName("Junit3Test 클래스의 메소드를 실행한다.")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            declaredMethod.invoke(clazz.newInstance());
        }

        List<String> names = Arrays.stream(declaredMethods)
                .map(Method::getName)
                .collect(Collectors.toList());
        assertThat(names).containsExactlyInAnyOrder("test1", "test2", "three");
    }
}
