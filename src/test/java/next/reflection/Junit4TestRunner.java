package next.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Junit4TestRunner {

    @DisplayName("@MyTest 애노테이션으로 설정되어 있는 메소드만 실행한다.")
    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Constructor<Junit4Test> constructor = clazz.getConstructor();

        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .collect(Collectors.toList());

        for (Method method : methods) {
            method.invoke(constructor.newInstance());
        }

        assertThat(methods).hasSize(2);
        assertThat(methods.get(0).getName()).isEqualTo("one");
        assertThat(methods.get(1).getName()).isEqualTo("two");
    }
}
