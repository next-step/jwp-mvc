package next.reflection;

import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AssignableParameterHelper {
    private static final Map<Class<?>, List<Class<?>>> assignableTypeMap = Maps.newHashMap();

    static {
        final List<Class<?>> intMap = Arrays.asList(int.class, Integer.class, long.class, Long.class);
        final List<Class<?>> longMap = Arrays.asList(long.class, Long.class);
        assignableTypeMap.put(int.class, intMap);
        assignableTypeMap.put(Integer.class, intMap);
        assignableTypeMap.put(long.class, longMap);
        assignableTypeMap.put(Long.class, longMap);
    }

    public static boolean isAssignable(Class<?> parameterType, Class<?> valueType) {
        if (parameterType.equals(valueType)) {
            return true;
        }

        final List<Class<?>> assignable = assignableTypeMap.get(valueType);
        return assignable.contains(parameterType);
    }
}
