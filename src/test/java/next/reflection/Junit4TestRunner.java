package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

class Junit4TestRunner {

    @Test
    void run() {
        final Class<Junit4Test> clazz = Junit4Test.class;

        Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(ExceptionWrapper.wrapper(method -> method.invoke(clazz.newInstance())));
    }
}
