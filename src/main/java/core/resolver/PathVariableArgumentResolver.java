package core.resolver;

import java.util.Map;

import core.annotation.web.PathVariable;
import core.di.factory.MethodParameter;
import core.handler.converter.StringConverters;
import core.mvc.WebRequest;

public class PathVariableArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {

        if (!StringConverters.getInstance().supports(methodParameter.getType())) {
            return false;
        }

        return methodParameter.hasAnnotation(PathVariable.class);
    }

    @Override
    public Object resolve(MethodParameter methodParameter, WebRequest webRequest) {
        Map<String, String> uriVariables = webRequest.getUriVariables();
        String uriVariable = uriVariables.get(methodParameter.getName());

        return StringConverters.getInstance()
                .getConverter(methodParameter.getType())
                .convert(uriVariable);
    }
}
