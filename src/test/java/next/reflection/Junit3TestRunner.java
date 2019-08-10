package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

class Junit3TestRunner {

    private static final String TEST = "test";

    @Test
    void run() {
        final Class<Junit3Test> clazz = Junit3Test.class;

        Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith(TEST))
                .forEach(ExceptionWrapper.wrapper(method -> method.invoke(clazz.newInstance())));
    }
}
