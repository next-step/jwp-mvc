package core.mvc.tobe.resolver;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArgumentModels {

    private static final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final ArgumentModels argumentModels = new ArgumentModels();

    public static ArgumentModels getInstance() {
        return argumentModels;
    }

    private ArgumentModels() {
    }

    public List<ArgumentModel> argumentModels(Method method) {
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        return IntStream.range(0, method.getParameterCount())
                .mapToObj(i -> new ArgumentModel(method, parameterTypes[i], parameterAnnotations[i], Objects.requireNonNull(parameterNames)[i]))
                .collect(Collectors.toUnmodifiableList());
    }

}
