package core.mvc.tobe.resolver;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

public abstract class AbstractArgumentResolver implements ArgumentResolver{
    protected static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    protected String getArgumentName(Method method, int index) {
        return nameDiscoverer.getParameterNames(method)[index];
    }

}
