package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class Junit4TestRunner {

    @DisplayName("요구사항 3 - @MyTest 애노테이션으로 설정되어 있는 메소드만 실행한다.")
    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .collect(Collectors.toList());

        for (Method method : methods) {
            method.invoke(clazz.getDeclaredConstructor().newInstance());
        }

        assertThat(methods).hasSize(2);
        assertThat(methods.get(0).getName()).isEqualTo("one");
        assertThat(methods.get(1).getName()).isEqualTo("two");
    }
}
