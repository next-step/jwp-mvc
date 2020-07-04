package core.mvc.tobe.argumentresolver;

import core.annotation.web.RequestMapping;
import core.mvc.tobe.argumentresolver.util.ParameterNameUtil;
import core.mvc.tobe.argumentresolver.util.PathVariableUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class MethodParameter {
    private Class<?> type;
    private String name;
    private Parameter parameter;
    private Method method;
    private String requestURI;

    public MethodParameter(Method method, int index) {
        this.type = findType(method, index);
        this.name = findName(method, index);
        this.parameter = method.getParameters()[index];
        this.method = method;
    }

    public MethodParameter(Method method, int index, String requestURI) {
        this(method, index);
        this.requestURI = requestURI;
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isHttpServletRequest() {
        return this.type.equals(HttpServletRequest.class);
    }

    public boolean isHttpServletResponse() {
        return this.type.equals(HttpServletResponse.class);
    }

    public boolean isJavaBeanType() {
        return (isPrimitiveType() == false) && (isStringType() == false);
    }

    public boolean isStringType() {
        return this.type.equals(String.class);
    }

    public boolean isIntegerType() {
        return type.equals(int.class) || type.equals(long.class);
    }

    public boolean hasPathVariable() {
        RequestMapping declaredAnnotation = this.method.getDeclaredAnnotation(RequestMapping.class);
        String path = declaredAnnotation.value();

        return PathVariableUtil.hasPathVariable(path, requestURI);
    }

    public String getPath() {
        return this.method.getAnnotation(RequestMapping.class).value();
    }

    private Class<?> findType(Method method, int index) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes[index];
    }

    private String findName(Method method, int index) {
        return ParameterNameUtil.getName(method, index);
    }

    private boolean isPrimitiveType() {
        return this.type.isPrimitive();
    }
}
