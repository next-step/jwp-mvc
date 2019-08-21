package core.mvc.tobe;

import java.util.function.Consumer;

@FunctionalInterface
public interface ExceptionConsumerSupplier<T> {
    void accept(T t) throws Exception;

    static <T> Consumer<T> wrap(ExceptionConsumerSupplier<T> f) {
        return (T r) -> {
            try {
                f.accept(r);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
