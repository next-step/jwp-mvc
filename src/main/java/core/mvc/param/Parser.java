package core.mvc.param;

public interface Parser<T> {
    boolean isParsable(Class<?> clazz);
    T parse(String value);
}
