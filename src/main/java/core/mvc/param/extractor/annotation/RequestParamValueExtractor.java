package core.mvc.param.extractor.annotation;

import core.annotation.web.RequestParam;
import core.mvc.param.Parameter;

import javax.servlet.http.HttpServletRequest;

// 지금은 필요 없나..?
public class RequestParamValueExtractor extends AnnotationValueExtractor<RequestParam> {

    protected RequestParamValueExtractor() {
        super(RequestParam.class);
    }

    @Override
    public Object extract(Parameter parameter, HttpServletRequest request) {
        return null;
    }
}
