package core.mvc.support;

import core.mvc.utils.StreamUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MethodSignature {

    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private final List<MethodParameter> methodParameters;

    public MethodSignature(Method method) {
        this.methodParameters = StreamUtils.zip(
                Arrays.asList(Objects.requireNonNull(nameDiscoverer.getParameterNames(method))),
                Arrays.asList(method.getParameters()),
                (name, parameter) -> new MethodParameter(method, new NamedParameter(parameter, name))
        );
    }

    public List<MethodParameter> getMethodParameters() {
        return methodParameters;
    }
}
