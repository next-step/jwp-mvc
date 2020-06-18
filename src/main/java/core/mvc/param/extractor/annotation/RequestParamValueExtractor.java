package core.mvc.param.extractor.annotation;

import core.annotation.web.RequestParam;
import core.exception.ParameterNotFoundException;
import core.mvc.param.Parameter;
import core.mvc.param.extractor.simple.TypeParser;

import javax.servlet.http.HttpServletRequest;

public class RequestParamValueExtractor extends AnnotationValueExtractor<RequestParam> {

    protected RequestParamValueExtractor() {
        super(RequestParam.class);
    }

    @Override
    public Object extract(Parameter parameter, HttpServletRequest request) {
        RequestParam requestParam = (RequestParam) parameter.getAnnotation();
        String attribute = getParameter(parameter, request, requestParam);
        Object extractedParam = TypeParser.parse(parameter.getTypeClass(), attribute);

        if (requestParam.required() && extractedParam == null) {
            throw new ParameterNotFoundException("Request param is not exist");
        }

        return extractedParam;
    }

    private String getParameter(Parameter parameter, HttpServletRequest request, RequestParam requestParam) {
        if (requestParam.value().isEmpty()) {
            return request.getParameter(parameter.getName());
        }

        return request.getParameter(requestParam.value());
    }
}
