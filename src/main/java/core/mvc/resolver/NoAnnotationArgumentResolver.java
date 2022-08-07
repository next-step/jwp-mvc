package core.mvc.resolver;

import com.google.common.primitives.Primitives;
import core.annotation.Component;
import core.mvc.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class NoAnnotationArgumentResolver implements MethodArgumentResolver {
    private static final Set<Object> WHITE_LIST;
    static {
        WHITE_LIST = Stream.of(Primitives.allPrimitiveTypes(), Primitives.allWrapperTypes())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        WHITE_LIST.add(String.class);
    }
    @Override
    public boolean support(MethodParameter parameter) {
        return !parameter.hasParameterAnnotations()
                && WHITE_LIST.contains(parameter.getParameterType());
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, String parameterName, MethodParameter parameter) {
        String foundParameter = request.getParameter(parameterName);

        return cast(parameter.getParameterType(), foundParameter);
    }
}
