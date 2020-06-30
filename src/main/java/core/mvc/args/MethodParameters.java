package core.mvc.args;

import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

/**
 * Created by iltaek on 2020/06/29 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class MethodParameters {

    private final List<MethodParameter> methodParameters = Lists.newArrayList();

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public MethodParameters(Method method) {
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            methodParameters.add(new MethodParameter(method, parameterNames[i], parameters[i]));
        }
    }

    public List<MethodParameter> getParameters() {
        return methodParameters;
    }
}
