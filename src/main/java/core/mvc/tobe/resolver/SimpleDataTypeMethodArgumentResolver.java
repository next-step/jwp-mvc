package core.mvc.tobe.resolver;

import core.mvc.tobe.MethodParameter;
import core.mvc.utils.DataParser;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleDataTypeMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return !methodParameter.hasAnnotation() && methodParameter.isSimpleDataType();
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return getArg(methodParameter.getName(), methodParameter.getType(), req);
    }

    private Object getArg(String name, Class<?> type, HttpServletRequest req) {
        DataParser dataParser = DataParser.from(type);
        return dataParser.parse(req.getParameter(name));
    }
}
