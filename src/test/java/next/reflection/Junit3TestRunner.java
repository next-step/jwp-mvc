package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class Junit3TestRunner {

    @Test
    void run() {
        final Class<Junit3Test> clazz = Junit3Test.class;
        final List<Method> methods = Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .collect(toList());

        methods.forEach(wrapper(method -> method.invoke(clazz.newInstance())));
    }

    private <T, E extends Exception> Consumer<T> wrapper(ConsumerWithException<T, E> ce) {
        return arg -> {
            try {
                ce.accept(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @FunctionalInterface
    private interface ConsumerWithException<T, E extends Exception> {
        void accept(T t) throws E;
    }
}
