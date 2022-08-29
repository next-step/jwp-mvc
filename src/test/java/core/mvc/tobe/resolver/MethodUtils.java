package core.mvc.tobe.resolver;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodUtils {
    static Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
