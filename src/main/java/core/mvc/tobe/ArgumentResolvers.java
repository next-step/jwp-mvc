package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ArgumentResolvers {

    private static final List<ArgumentResolver> RESOLVERS = new ArrayList<>();

    static {
        RESOLVERS.add(new HttpServletRequestArgumentResolver());
        RESOLVERS.add(new HttpServletResponseArgumentResolver());
        RESOLVERS.add(new RequestParamMethodArgumentResolver());
        RESOLVERS.add(new ModelAttributeMethodArgumentResolver());
        RESOLVERS.add(new PathVariableMethodArgumentResolver());
    }

    public Object resolve(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return RESOLVERS.stream()
            .filter(resolver -> resolver.supportsParameter(parameter))
            .findFirst()
            .orElseThrow(IllegalStateException::new)
            .resolveArgument(parameter, request, response);
    }
}
