package core.mvc.tobe.PrimitiveType;

/**
 * Created By kjs4395 on 2020-06-29
 */
public interface TypeChecker {
    boolean isSupportType(Class<?> clazz);

    Object parseType(String value);
}
