package core.mvc.tobe;

import java.util.function.Function;

@FunctionalInterface
public interface ExceptionFunctionSupplier<T, R> {
    R apply(T r) throws Exception;

    static <T, R> Function<T, R> wrap(ExceptionFunctionSupplier<T, R> f) {
        return (T r) -> {
            try {
                return f.apply(r);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
