package core.utils.typeresolver;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class ShortTypeResolver implements TypeResolver {

    protected static final String ILLEGAL_SHORT = "유효하지 않은 Short 값입니다.: ";

    @Override
    public boolean isSupport(Class<?> type) {
        return short.class == type || Short.class == type;
    }

    @Override
    public Object resolve(String item) {
        try {
            return Short.valueOf(item);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(ILLEGAL_SHORT + item);
        }
    }
}
