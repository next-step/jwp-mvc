package core.mvc.args.resolver;

import core.mvc.args.MethodParameter;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class RequestMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean isSupport(MethodParameter parameter) {
        return parameter.getType() == HttpServletRequest.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        return request;
    }
}
