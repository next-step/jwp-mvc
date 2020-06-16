package core.mvc.param.extractor;

import core.mvc.param.Parameter;

import javax.servlet.http.HttpServletRequest;

public interface ValueExtractor {
    Object extract(Parameter parameter, HttpServletRequest request);
}
