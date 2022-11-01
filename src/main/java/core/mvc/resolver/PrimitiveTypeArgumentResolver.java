package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.mvc.tobe.MethodParameter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @RequestParam 또는 일반 Primitive Type 변수도 처리할 수 있는 Resolver
 */
public class PrimitiveTypeArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();

        /**
         * PathVariable 어노테이션이 있으면 PathVariableArgumentResolver 가 호출되어야한다.
         */
        if (parameter.getAnnotations().equals(PathVariable.class))
            return false;

        return (parameterType.isPrimitive() || parameterType.equals(String.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Class<?> parameterType = parameter.getParameterType();
        String parameterName = parameter.getParameterName();
        String object = request.getParameter(parameterName);

        /**
         * RequestParam은 url 끝에 query String을 통해 데이터를 요청한다.
         */
        if (!StringUtils.hasText(object)) {
            if (isQueryString(request, parameterName))
                return ResolverUtil.convertPrimitiveType(parameterType, object);
        }

        return ResolverUtil.convertPrimitiveType(parameterType,object);
    }

    private boolean isQueryString(HttpServletRequest request,String parameterName) throws IOException {
        String readLine = request.getReader().readLine();

        if(readLine == null) {
            return false;
        }

        for (String param : readLine.split("&")) {
            String[] pair = param.split("=");
            if (pair[0].equals(parameterName)) {
                return true;
            }
        }
        return false;
    }
}
