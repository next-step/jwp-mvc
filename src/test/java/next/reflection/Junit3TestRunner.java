package next.reflection;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class Junit3TestRunner {

    @DisplayName("Junit3Test에서 test로 시작하는 메서드 만을 실행")
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();
        List<Method> methodsStartWithTest = Stream.of(clazz.getDeclaredMethods())
                .filter(method -> StringUtils.startsWith(method.getName(), "test"))
                .collect(Collectors.toList());

        for (Method method : methodsStartWithTest) {
            method.invoke(junit3Test);
        }

        assertAll(
                () -> assertThat(methodsStartWithTest.size()).isEqualTo(2),
                () -> assertThat(methodsStartWithTest.get(0).getName()).startsWith("test"),
                () -> assertThat(methodsStartWithTest.get(1).getName()).startsWith("test")
        );
    }
}
