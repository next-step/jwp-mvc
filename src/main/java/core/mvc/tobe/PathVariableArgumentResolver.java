package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class PathVariableArgumentResolver extends AbstractArgumentResolver {
    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private Class clazz;
    private Method method;

    public PathVariableArgumentResolver(final Class clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
        applyExecution(method, this);
    }

    @Override
    public void applyExecution(final Method method, final HandlerExecution handlerExecution) {
        super.applyExecution(method, handlerExecution);
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        if (Objects.nonNull(annotation)) {
            PathPattern pp = parse(annotation.value());
            if (pp.matches(toPathContainer(request.getRequestURI()))) {
                final Map<String, String> uriVariables = pp.matchAndExtract(toPathContainer(request.getRequestURI()))
                        .getUriVariables();

                String[] parameterNames = nameDiscoverer.getParameterNames(method);
                Object[] values = new Object[parameterNames.length];
                for (int i = 0; i < parameterNames.length; i++) {
                    String parameterName = parameterNames[i];
                    String value = uriVariables.get(parameterName);
                    values[i] = value;
                    for (final Class<?> parameterType : method.getParameterTypes()) {
                        if (parameterType.equals(int.class)) {
                            values[i] = Integer.parseInt(value);
                        }
                        if (parameterType.equals(long.class)) {
                            values[i] = Long.parseLong(value);
                        }
                    }
                }
                return  (ModelAndView) method.invoke(clazz.newInstance(), values);
            }
        }
        return super.handle(request, response);
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
