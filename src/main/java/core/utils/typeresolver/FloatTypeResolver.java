package core.utils.typeresolver;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class FloatTypeResolver implements TypeResolver {

    protected static final String ILLEGAL_FLOAT = "유효하지 않은 Float 값입니다.: ";

    @Override
    public boolean isSupport(Class<?> type) {
        return float.class == type || Float.class == type;
    }

    @Override
    public Object resolve(String item) {
        try {
            return Float.valueOf(item);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(ILLEGAL_FLOAT + item);
        }
    }
}
