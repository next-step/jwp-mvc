package core.mvc.tobe.PrimitiveType;

/**
 * Created By kjs4395 on 2020-06-29
 */
public class IntegerTypeChecker implements TypeChecker {

    @Override
    public boolean isSupportType(Class<?> clazz) {
        return clazz.equals(int.class);
    }

    @Override
    public Object parseType(String value) {
        return Integer.parseInt(value);
    }
}
