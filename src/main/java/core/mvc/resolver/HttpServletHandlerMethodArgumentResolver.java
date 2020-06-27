package core.mvc.resolver;

import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServletHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Parameter parameter = methodParameter.toParameter();
        if(parameter.getType().isAssignableFrom(HttpServletRequest.class)){
            return true;
        }

        if(parameter.getType().isAssignableFrom(HttpServletResponse.class)){
            return true;
        }

        return false;
    }

    @Override
    public Object resolve(MethodParameter methodParameter, String mappingUrl, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        if(!supportsParameter(methodParameter)){
            throw new IllegalArgumentException("unSupports Parameter");
        }

        Parameter parameter = methodParameter.toParameter();
        if(parameter.getType().isAssignableFrom(HttpServletRequest.class)){
            return httpRequest;
        }


        if(parameter.getType().isAssignableFrom(HttpServletResponse.class)){
            return httpResponse;
        }

        throw new IllegalArgumentException("unSupports Parameter");
    }

}
