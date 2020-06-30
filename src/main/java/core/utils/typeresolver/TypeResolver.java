package core.utils.typeresolver;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public interface TypeResolver {

    boolean isSupport(Class<?> type);

    Object resolve(String item);
}
