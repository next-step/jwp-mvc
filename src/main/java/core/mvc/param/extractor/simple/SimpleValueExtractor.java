package core.mvc.param.extractor.simple;

import core.mvc.param.Parameter;
import core.mvc.param.extractor.ValueExtractor;

import javax.servlet.http.HttpServletRequest;

public class SimpleValueExtractor implements ValueExtractor {
    @Override
    public Object extract(Parameter parameter, HttpServletRequest request) {
        String value = request.getParameter(parameter.getName());

        if (value == null) {
            return null;
        }

        return TypeParser.parse(parameter.getTypeClass(), value);
    }
}
