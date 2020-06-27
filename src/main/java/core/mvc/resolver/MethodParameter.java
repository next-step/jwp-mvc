package core.mvc.resolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

public class MethodParameter {
    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private final Method method;
    private final int index;

    public MethodParameter(Method method, int index) {
        this.method = method;
        this.index = index;
    }

    public Parameter toParameter(){
        return method.getParameters()[index];
    }

    public String getParameterName() {
        return nameDiscoverer.getParameterNames(method)[index];
    }

    public Method getMethod() {
        return this.method;
    }
}
