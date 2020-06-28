package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

import static core.mvc.tobe.ParameterUtils.isHttpRequestType;

public class HttpRequestArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean support(Method method) {
        return isHttpRequestType(method);
    }

    @Override
    public Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response) {
        return Arrays.asList(request, response).toArray();
    }

}
