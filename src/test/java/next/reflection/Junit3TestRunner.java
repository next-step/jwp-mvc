package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class Junit3TestRunner {
    @DisplayName("Requirement - 2 : test로 작하는 메소드 실행")
    @Test
    public void run() throws Exception {
        //given
        Class<Junit3Test> clazz = Junit3Test.class;

        //when
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .collect(toList());

        //then
        assertThat(methods).hasSize(2);
        methods.stream()
                .forEach(method -> invokeMethod(clazz, method));
    }

    private void invokeMethod(Class clazz, Method method) {
        try {
            method.invoke(clazz.newInstance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
