package core.mvc.param;

import core.exception.ParameterNotFoundException;
import core.mvc.param.parser.TypeParser;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

public class Parameter {
    private final Class<?> type;
    private final String name;
    private final Class<? extends Annotation> annotation; // 활용은..?

    public Parameter(String name, Class<?> type, Class<? extends Annotation> annotation) {
        this.name = name;
        this.type = type;
        this.annotation = annotation;
    }

    public Object searchParam(final HttpServletRequest request) {
        String parameter = request.getParameter(name);

        if (parameter == null) {
            throw new ParameterNotFoundException(name);
        }

        return TypeParser.parse(type, parameter);
    }
}
