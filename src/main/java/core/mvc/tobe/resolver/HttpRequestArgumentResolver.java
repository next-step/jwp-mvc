package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpRequestArgumentResolver implements ArgumentResolver {

    private static final HttpRequestArgumentResolver httpRequestArgumentResolver = new HttpRequestArgumentResolver();

    private HttpRequestArgumentResolver() {}

    public static HttpRequestArgumentResolver getInstance() {
        return httpRequestArgumentResolver;
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, ArgumentModel argumentModel) {
        return request;
    }

    @Override
    public boolean isSupport(ArgumentModel argumentModel) {
        return argumentModel.type().equals(HttpServletRequest.class);
    }

}
