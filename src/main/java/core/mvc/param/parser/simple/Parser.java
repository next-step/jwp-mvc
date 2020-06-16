package core.mvc.param.parser.simple;

public interface Parser<T> {
    boolean isParsable(Class<?> clazz);
    T parse(String value);
}
