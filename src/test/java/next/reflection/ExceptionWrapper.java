package next.reflection;

import java.util.function.Consumer;

class ExceptionWrapper {

    static <T, E extends Exception> Consumer<T> wrapper(ConsumerWithException<T, E> ce) {
        return arg -> {
            try {
                ce.accept(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
