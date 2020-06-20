package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Junit4TestRunner {

    @DisplayName("Junit4Test 클래스의 어노테이션이 있는 메소드를 실행한다.")
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Method[] declaredMethods = clazz.getDeclaredMethods();
        List<Method> methods = Arrays.stream(declaredMethods)
                .filter(m -> m.isAnnotationPresent(MyTest.class))
                .collect(Collectors.toList());

        for (Method method : methods) {
            method.invoke(clazz.newInstance());
        }

        List<String> names = methods.stream()
                .map(Method::getName)
                .collect(Collectors.toList());

        assertThat(names).containsExactlyInAnyOrder("one", "two");
    }
}
