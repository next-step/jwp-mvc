package core.utils.typeresolver;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class CharacterTypeResolver implements TypeResolver {

    protected static final String ILLEGAL_CHAR = "유효하지 않은 Character 값입니다.: ";

    @Override
    public boolean isSupport(Class<?> type) {
        return char.class == type || Character.class == type;
    }

    @Override
    public Object resolve(String item) {
        if (!isValidItem(item)) {
            throw new IllegalArgumentException(ILLEGAL_CHAR + item);
        }
        return item.charAt(0);
    }

    private boolean isValidItem(String item) {
        return item.length() == 1;
    }
}
