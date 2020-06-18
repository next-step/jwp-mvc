package core.mvc.param.extractor.simple;

public interface Parser<T> {
    boolean isParsable(Class<?> clazz);
    T parse(String value);
}
