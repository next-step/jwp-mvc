package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        //given
        Class<Junit4Test> clazz = Junit4Test.class;

        //when
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .collect(toList());

        //then
        assertThat(methods).hasSize(2);
        methods.stream()
                .forEach(method -> {
                    try {
                        method.invoke(clazz.newInstance());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                });
    }
}
