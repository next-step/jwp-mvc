package core.utils.typeresolver;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class BoolTypeResolver implements TypeResolver {

    protected static final String ILLEGAL_BOOLEAN = "유효하지 않은 Boolean 값입니다.: ";

    @Override
    public boolean isSupport(Class<?> type) {
        return boolean.class == type || Boolean.class == type;
    }

    @Override
    public Object resolve(String item) {
        if (!isBooleanString(item)) {
            throw new IllegalArgumentException(ILLEGAL_BOOLEAN + item);
        }
        return Boolean.valueOf(item);
    }

    private boolean isBooleanString(String item) {
        String lowerItem = item.toLowerCase();
        return "true".equals(lowerItem) || "false".equals(lowerItem);
    }
}
