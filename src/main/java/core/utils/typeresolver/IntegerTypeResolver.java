package core.utils.typeresolver;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class IntegerTypeResolver implements TypeResolver {

    protected static final String ILLEGAL_INTEGER = "유효하지 않은 Integer 값입니다.: ";

    @Override
    public boolean isSupport(Class<?> type) {
        return int.class == type || Integer.class == type;
    }

    @Override
    public Object resolve(String item) {
        try {
            return Integer.parseInt(item);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(ILLEGAL_INTEGER + item);
        }
    }
}
