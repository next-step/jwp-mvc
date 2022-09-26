package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpResponseArgumentResolver implements ArgumentResolver {

    private static final HttpResponseArgumentResolver httpResponseArgumentResolver = new HttpResponseArgumentResolver();

    private HttpResponseArgumentResolver() {}

    public static HttpResponseArgumentResolver getInstance() {
        return httpResponseArgumentResolver;
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, ArgumentModel argumentModel) {
        return response;
    }

    @Override
    public boolean isSupport(ArgumentModel argumentModel) {
        return argumentModel.type().equals(HttpServletResponse.class);
    }

}
