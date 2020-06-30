package core.utils.typeresolver;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class LongTypeResolver implements TypeResolver {

    protected static final String ILLEGAL_LONG = "유효하지 않은 Long 값입니다.: ";

    @Override
    public boolean isSupport(Class<?> type) {
        return long.class == type || Long.class == type;
    }

    @Override
    public Object resolve(String item) {
        try {
            return Long.valueOf(item);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(ILLEGAL_LONG + item);
        }
    }
}
