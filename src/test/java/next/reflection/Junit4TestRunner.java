package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(MyTest.class))
                .forEach(it -> {
                    try {
                        it.invoke(clazz.newInstance());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
