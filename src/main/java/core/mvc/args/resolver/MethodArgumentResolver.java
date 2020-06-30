package core.mvc.args.resolver;

import core.mvc.args.MethodParameter;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by iltaek on 2020/06/29 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public interface MethodArgumentResolver {

    boolean isSupport(MethodParameter parameter);

    Object resolveArgument(MethodParameter parameter, HttpServletRequest request);
}
