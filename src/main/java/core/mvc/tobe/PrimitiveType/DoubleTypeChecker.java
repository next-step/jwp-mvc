package core.mvc.tobe.PrimitiveType;

/**
 * Created By kjs4395 on 2020-06-29
 */
public class DoubleTypeChecker implements TypeChecker {
    @Override
    public boolean isSupportType(Class<?> clazz) {
        return clazz.equals(double.class);
    }

    @Override
    public Object parseType(String value) {
        return Double.parseDouble(value);
    }
}
