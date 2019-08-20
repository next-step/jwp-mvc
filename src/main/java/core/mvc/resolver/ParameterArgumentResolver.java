package core.mvc.resolver;

import com.google.common.collect.Sets;
import core.mvc.MethodParameter;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParameterArgumentResolver extends AbstractHandlerMethodArgumentResolver {

    private ParameterNameDiscoverer parameterNameDiscoverer;

    public ParameterArgumentResolver() {
        parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    }

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.isAnnotationNotExist();
    }

    @Override
    public Object getMethodArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getNewInstance(parameter, request);
    }

    private Object getNewInstance(MethodParameter parameter, HttpServletRequest request) throws Exception {
        Map<String, String[]> requestParameters = request.getParameterMap();
        Class<?> parameterType = parameter.getType();
        Constructor<?>[] constructors = parameterType.getConstructors();

        for (Constructor<?> constructor : constructors) {
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(constructor);

            List<MethodParameter> methodParameters = getMethodParameters(constructor);
            Object[] objects = getConstructorArguments(methodParameters, requestParameters);

            if (isConstructorMatch(requestParameters.keySet(), parameterNames)) {
                return constructor.newInstance(objects);
            }
        }

        throw new Exception("Can't find matched constructor");
    }

    private List<MethodParameter> getMethodParameters(Constructor<?> constructor) {
        final Class<?>[] types = constructor.getParameterTypes();
        String[] names = parameterNameDiscoverer.getParameterNames(constructor);
        assert names != null;

        return IntStream.range(0, names.length)
                .mapToObj(i -> new MethodParameter(names[i], types[i]))
                .collect(Collectors.toList());
    }

    private Object[] getConstructorArguments(List<MethodParameter> parameters, Map<String, String[]> requestParameters) throws Exception {
        Object[] objects = new Object[parameters.size()];
        for(int i = 0; i < parameters.size(); i++) {
            MethodParameter methodParameter = parameters.get(i);
            objects[i] = getArgument(methodParameter, requestParameters.get(methodParameter.getName())[0]);
        }

        return objects;
    }

    private boolean isConstructorMatch(Set<String> parameters, String[] parameterNames) {
        Set<String> parameterName = Sets.newHashSet(parameterNames);

        return parameterName.containsAll(parameters);
    }
}
