package next.reflection;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ReflectionUtils {

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz, Object... args) {
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        return (T) Arrays.stream(declaredConstructors)
                .filter(constructor -> constructor.getParameters().length == args.length)
                .findFirst()
                .map(it -> {
                    try {
                        return it.newInstance(args);
                    } catch (Exception e) {
                        throw new IllegalStateException("객체 생성을 실패하였습니다.", e);
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("인자값이 일치하지 않습니다."));
    }
}