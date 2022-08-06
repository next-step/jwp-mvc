package next.reflection;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class Junit3TestRunner {

    @Test
    @DisplayName("test 로 시작하는 메소드 실행")
    void run() throws Exception {
        //given
        PrintStream print = mock(PrintStream.class);
        System.setOut(print);

        Class<Junit3Test> junit3TestClass = Junit3Test.class;
        Junit3Test junit3Test = junit3TestClass.getDeclaredConstructor().newInstance();
        List<Method> startWithTestMethodsOfJunit3Test = Stream.of(junit3TestClass.getDeclaredMethods())
                .filter(method -> StringUtils.startsWith(method.getName(), "test"))
                .collect(Collectors.toList());
        //when
        for (Method method : startWithTestMethodsOfJunit3Test) {
            method.invoke(junit3Test);
        }
        //then
        assertAll(
                () -> verify(print, times(1)).println("Running Test1"),
                () -> verify(print, times(1)).println("Running Test2"),
                () -> verify(print, never()).println("Running Test3")
        );
    }
}
