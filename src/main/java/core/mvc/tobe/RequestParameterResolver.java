package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.mvc.tobe.extractor.RequestParameterExtractor;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Objects;

public class RequestParameterResolver {

    private final ParameterNameDiscoverer parameterNameDiscoverer;
    private final List<RequestParameterExtractor> requestParameterExtractors;

    public RequestParameterResolver(final ParameterNameDiscoverer parameterNameDiscoverer,
                                    final List<RequestParameterExtractor> requestParameterExtractors) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
        this.requestParameterExtractors = requestParameterExtractors;
    }

    Object[] resolve(final Method method,
                     final HttpServletRequest request,
                     final HttpServletResponse response) {
        final Parameter[] parameters = method.getParameters();
        final String[] parameterNames = Objects.requireNonNull(parameterNameDiscoverer.getParameterNames(method));

        final Object[] resolvedParameters = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            final Parameter parameter = parameters[i];
            final String parameterName = parameterNames[i];
            final String mappingPath = method.getAnnotation(RequestMapping.class).value();

            final ParameterInfo parameterInfo = new ParameterInfo(parameter, parameterName, mappingPath);

            resolvedParameters[i] = extract(request, response, parameterInfo);
        }

        return resolvedParameters;
    }

    private Object extract(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final ParameterInfo parameterInfo) {
        return requestParameterExtractors.stream()
                .filter(extractor -> extractor.isSupport(parameterInfo))
                .findFirst()
                .map(extractor -> extractor.extract(parameterInfo, request, response))
                .orElse(null);
    }
}
