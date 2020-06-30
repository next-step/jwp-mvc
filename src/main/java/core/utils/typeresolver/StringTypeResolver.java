package core.utils.typeresolver;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class StringTypeResolver implements TypeResolver {

    @Override
    public boolean isSupport(Class<?> type) {
        return String.class == type;
    }

    @Override
    public Object resolve(String item) {
        return item;
    }
}
