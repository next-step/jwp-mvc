package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class Junit4TestRunner {

    @DisplayName("Junit4Test에서 @MyTest 어노테이션이 있는 메서드만 실행")
    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();
        List<Method> methodsContainAnnotation = Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .collect(Collectors.toList());

        for (Method method : methodsContainAnnotation) {
            method.invoke(junit4Test);
        }

        assertAll(
                () -> assertThat(methodsContainAnnotation.size()).isEqualTo(2),
                () -> assertThat(methodsContainAnnotation.get(0).getAnnotation(MyTest.class)).isNotNull(),
                () -> assertThat(methodsContainAnnotation.get(1).getAnnotation(MyTest.class)).isNotNull()
        );
    }
}
