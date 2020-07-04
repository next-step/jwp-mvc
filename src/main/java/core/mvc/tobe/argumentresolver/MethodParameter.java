package core.mvc.tobe.argumentresolver;

import java.lang.reflect.Method;

public class MethodParameter {
    private Class<?> type;
    private String name;

    public MethodParameter(Method method, int index) {
        this.type = findType(method, index);
        this.name = findName(method, index);
    }

    private Class<?> findType(Method method, int index) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes[index];
    }

    private String findName(Method method, int index) {
        return ParameterNameUtils.getName(method, index);
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
