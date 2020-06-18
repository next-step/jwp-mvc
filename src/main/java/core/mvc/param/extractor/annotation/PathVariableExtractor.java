package core.mvc.param.extractor.annotation;

import core.annotation.web.PathVariable;
import core.mvc.param.Parameter;
import core.mvc.param.extractor.simple.TypeParser;

import javax.servlet.http.HttpServletRequest;

public class PathVariableExtractor extends AnnotationValueExtractor<PathVariable> {

    protected PathVariableExtractor() {
        super(PathVariable.class);
    }

    public Object extract(Parameter parameter, HttpServletRequest request) {
        String attribute = (String) request.getAttribute(parameter.getName());

        return TypeParser.parse(parameter.getTypeClass(), attribute);
    }
}
