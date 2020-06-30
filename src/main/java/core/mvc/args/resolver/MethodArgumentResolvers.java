package core.mvc.args.resolver;

import com.google.common.collect.Lists;
import core.mvc.args.MethodParameter;
import core.mvc.args.MethodParameters;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by iltaek on 2020/06/29 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class MethodArgumentResolvers {

    private static final String NO_RESOLVER = "Method의 파라미터를 resolve할 수 없습니다.: ";
    private static final List<MethodArgumentResolver> resolvers = Lists.newArrayList();

    static {
        resolvers.add(new RequestMethodArgumentResolver());
        resolvers.add(new BeanMethodArgumentResolver());
        resolvers.add(new PathVariableMethodArgumentResolver());
        resolvers.add(new PrimitiveMethodArgumentResolver());
    }

    private MethodArgumentResolvers() {
    }

    public static Object[] resolveArguments(MethodParameters parameters, HttpServletRequest request) {
        return parameters.getParameters().stream()
            .map(parameter -> resolveArgument(parameter, request)).toArray();
    }

    private static Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        MethodArgumentResolver resolver = getResolver(parameter);
        return resolver.resolveArgument(parameter, request);
    }

    private static MethodArgumentResolver getResolver(MethodParameter parameter) {
        return resolvers.stream()
            .filter(resolver -> resolver.isSupport(parameter))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(NO_RESOLVER + parameter));
    }
}
