package next.reflection;

import java.util.function.Consumer;
import java.util.function.Function;

class ExceptionWrapper {

    static <T, E extends Exception> Consumer<T> consumerWrapper(ConsumerWithException<T, E> ce) {
        return arg -> {
            try {
                ce.accept(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @FunctionalInterface
    public interface ConsumerWithException<T, E extends Exception> {
        void accept(T t) throws E;
    }

    static <T, R, E extends Exception> Function<T, R> functionWrapper(FunctionWithException<T, R, E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @FunctionalInterface
    public interface FunctionWithException<T, R, E extends Exception> {
        R apply(T t) throws E;
    }
}
