package core.mvc.param.extractor.context;

import core.mvc.param.Parameter;
import core.mvc.param.extractor.ValueExtractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ContextValueExtractor implements ValueExtractor {
    public static String CONTEXTS = "contexts";

    @Override
    public Object extract(final Parameter parameter, final HttpServletRequest request) {
        Map<Class<?>, Object> contexts = (Map<Class<?>, Object>) request.getAttribute(CONTEXTS);

        return contexts.get(parameter.getTypeClass());
    }
}
