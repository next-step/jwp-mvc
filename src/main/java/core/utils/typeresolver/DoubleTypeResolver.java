package core.utils.typeresolver;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class DoubleTypeResolver implements TypeResolver {

    protected static final String ILLEGAL_DOUBLE = "유효하지 않은 Double 값입니다.: ";

    @Override
    public boolean isSupport(Class<?> type) {
        return double.class == type || Double.class == type;
    }

    @Override
    public Object resolve(String item) {
        try {
            return Double.valueOf(item);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(ILLEGAL_DOUBLE + item);
        }
    }
}
