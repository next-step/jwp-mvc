package core.mvc.param.extractor.type;

import core.mvc.param.Parameter;
import core.mvc.param.extractor.ValueExtractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TypeValueExtractor implements ValueExtractor {
    public static String TYPES = "types";

    @Override
    public Object extract(Parameter parameter, HttpServletRequest request) {
        Map<Class<?>, Object> types = (Map<Class<?>, Object>) request.getAttribute(TYPES);

        return types.get(parameter.getTypeClass());
    }
}
