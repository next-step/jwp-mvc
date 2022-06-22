package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(it -> it.getName().startsWith("test"))
                .forEach(it -> {
                    try {
                        it.invoke(clazz.newInstance());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
