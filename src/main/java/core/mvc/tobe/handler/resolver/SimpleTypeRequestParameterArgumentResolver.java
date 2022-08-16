package core.mvc.tobe.handler.resolver;

import core.annotation.web.RequestParam;
import core.mvc.tobe.handler.resolver.utils.SimpleTypeConverter;
import core.mvc.tobe.handler.resolver.utils.TypeUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class SimpleTypeRequestParameterArgumentResolver extends AbstractNamedSimpleTypeArgumentResolver implements ArgumentResolver {
    public SimpleTypeRequestParameterArgumentResolver(SimpleTypeConverter simpleTypeConverter) {
        super(simpleTypeConverter);
    }

    @Override
    public boolean support(NamedParameter parameter) {
        return TypeUtils.isSimpleType(parameter.getType());
    }

    @Override
    protected String getParameterName(NamedParameter parameter) {
        RequestParam requestParam = parameter.getParameter().getAnnotation(RequestParam.class);
        String argumentName = parameter.getName();

        if (Objects.isNull(requestParam)) {
            return argumentName;
        }

        String name = requestParam.value();
        if (!name.isEmpty()) {
            return name;
        }

        return argumentName;
    }

    @Override
    protected String getNamedValue(NamedParameter parameter, HttpServletRequest request, String parameterName) {
        return request.getParameter(parameterName);
    }
}
