package core.mvc.tobe.argumentresolver;

import core.annotation.web.RequestMapping;
import core.mvc.tobe.argumentresolver.util.ParameterNameUtil;

import java.lang.reflect.Method;

public class MethodParameter {
    private Class<?> type;
    private String name;
    private Method method;
    private String requestURI;

    public MethodParameter(Method method, int index) {
        this.type = findType(method, index);
        this.name = findName(method, index);
        this.method = method;
    }

    public MethodParameter(Method method, int index, String requestURI) {
        this(method, index);
        this.requestURI = requestURI;
    }

    public String getPath() {
        return this.method.getAnnotation(RequestMapping.class).value();
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Method getMethod() {
        return method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    private Class<?> findType(Method method, int index) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes[index];
    }

    private String findName(Method method, int index) {
        return ParameterNameUtil.getName(method, index);
    }
}
