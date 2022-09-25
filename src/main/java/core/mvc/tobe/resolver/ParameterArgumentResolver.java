package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParameterArgumentResolver implements ArgumentResolver {

    private static final ParameterArgumentResolver parameterArgumentResolver = new ParameterArgumentResolver();

    private ParameterArgumentResolver() {}

    public static ParameterArgumentResolver getInstance() {
        return parameterArgumentResolver;
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, ArgumentModel argumentModel) {
        String parameter = request.getParameter(argumentModel.parameterName());
        if (parameter != null) {
            return parameter;
        }
        return request;
    }

    @Override
    public boolean isSupport(ArgumentModel argumentModel) {
        return argumentModel.type().equals(HttpServletRequest.class);
    }
}
