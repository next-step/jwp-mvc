package core.mvc.tobe.PrimitiveType;

/**
 * Created By kjs4395 on 2020-06-29
 */
public class ShortTypeChecker implements TypeChecker {
    @Override
    public boolean isSupportType(Class<?> clazz) {
        return clazz.equals(short.class);
    }

    @Override
    public Object parseType(String value) {
        return Short.parseShort(value);
    }
}
