package core.mvc.resolver;

import core.annotation.Component;
import core.annotation.web.RequestParam;
import core.mvc.exception.NoSuchArgumentResolverException;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class RequestParamArgumentResolver implements MethodArgumentResolver {

    public static final String STRING_EMPTY = "";

    @Override
    public boolean support(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestParam.class);
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, String parameterName, MethodParameter parameter) {
        RequestParam ano = parameter.getParameterAnnotation(RequestParam.class);

        if (!STRING_EMPTY.equals(Objects.requireNonNull(ano).name())) {
            parameterName = ano.name();
        }

        String result = request.getParameter(parameterName);

        if (Objects.isNull(result) && ano.required()) {
            throw new NoSuchArgumentResolverException(parameter);
        }

        return cast(parameter.getParameterType(),result);
    }
}
