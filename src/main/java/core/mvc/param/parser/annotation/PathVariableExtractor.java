package core.mvc.param.parser.annotation;

import core.annotation.web.PathVariable;
import core.mvc.param.Parameter;
import core.mvc.param.parser.simple.TypeParser;

import javax.servlet.http.HttpServletRequest;

public class PathVariableExtractor extends AnnotationValueExtractor<PathVariable> {

    protected PathVariableExtractor() {
        super(PathVariable.class);
    }

    // value of path pattern exist in attribute
    public Object extract(Parameter parameter, HttpServletRequest request) {
        String attribute = (String) request.getAttribute(parameter.getName());

        return TypeParser.parse(parameter.getTypeClass(), attribute);
    }
}
