package core.mvc.tobe.argumentresolver.util;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

public class ParameterNameUtil {
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    public static final String getName(Method method, int index){
        String[] parameterNames = NAME_DISCOVERER.getParameterNames(method);
        return parameterNames[index];
    }
}
