package core.utils.typeresolver;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class ByteTypeResolver implements TypeResolver {

    @Override
    public boolean isSupport(Class<?> type) {
        return byte.class == type || Byte.class == type;
    }

    @Override
    public Object resolve(String item) {
        return item.getBytes();
    }
}
