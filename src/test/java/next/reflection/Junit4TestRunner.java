package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class Junit4TestRunner {

    @Test
    @DisplayName("@MyTest 애노테이션 설정된 메소드 호출")
    void run() throws Exception {
        //given
        PrintStream print = mock(PrintStream.class);
        System.setOut(print);

        Class<Junit4Test> junit4TestClass = Junit4Test.class;
        Junit4Test junit4Test = junit4TestClass.getDeclaredConstructor().newInstance();
        List<Method> methodsWithMyTestAnnotation = Stream.of(junit4TestClass.getDeclaredMethods())
                .filter(this::isDeclaredMyTestAnnotation)
                .collect(Collectors.toList());
        //when
        for (Method method : methodsWithMyTestAnnotation) {
            method.invoke(junit4Test);
        }
        //then
        assertAll(
                () -> verify(print, times(1)).println("Running Test1"),
                () -> verify(print, times(1)).println("Running Test2"),
                () -> verify(print, never()).println("Running Test3")
        );
    }

    private boolean isDeclaredMyTestAnnotation(Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .anyMatch(annotation -> annotation.annotationType().isAssignableFrom(MyTest.class));
    }
}
