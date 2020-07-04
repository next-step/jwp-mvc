package core.mvc.tobe.argumentresolver;

import core.annotation.web.PathVariable;
import core.mvc.tobe.argumentresolver.util.ParameterNameUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter {
    private Class<?> type;
    private String name;
    private Parameter parameter;

    public MethodParameter(Method method, int index) {
        this.type = findType(method, index);
        this.name = findName(method, index);
        this.parameter = method.getParameters()[index];
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isHttpServletRequest(){
        return this.type.equals(HttpServletRequest.class);
    }

    public boolean isHttpServletResponse(){
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

    public boolean hasPathVariable(){
        PathVariable[] pathVariables = this.type.getDeclaredAnnotationsByType(PathVariable.class);

        if(pathVariables.length == 0){
            return false;
        }

        return true;
    }

    private Class<?> findType(Method method, int index) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes[index];
    }

    private String findName(Method method, int index) {
        return ParameterNameUtils.getName(method, index);
    }

    private boolean isPrimitiveType() {
        return this.type.isPrimitive();
    }
}
