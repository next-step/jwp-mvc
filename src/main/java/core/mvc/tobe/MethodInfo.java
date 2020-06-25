package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.mvc.tobe.helper.Helpers;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.util.pattern.PathPattern;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By kjs4395 on 2020-06-25
 */
public class MethodInfo {
    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private List<ParameterInfo> parameters = new ArrayList<>();

    public MethodInfo(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] names = nameDiscoverer.getParameterNames(method);
        PathPattern pathPattern = PathPatternUtil.getPathPattern(method.getDeclaredAnnotation(RequestMapping.class).value());
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterTypes.length; i++) {
            parameters.add(new ParameterInfo(parameterTypes[i], names[i], pathPattern, parameterAnnotations[i]));
        }
    }

    public Object[] bindingParameters(HttpServletRequest request) {
        return this.parameters
                .stream()
                .map(parameter -> Helpers.executeHelper(parameter,request))
                .toArray();
    }
}
