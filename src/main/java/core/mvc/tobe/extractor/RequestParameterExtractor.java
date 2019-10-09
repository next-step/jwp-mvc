package core.mvc.tobe.extractor;

import core.mvc.tobe.ParameterInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestParameterExtractor {

    boolean isSupport(final ParameterInfo parameterInfo);

    Object extract(final ParameterInfo parameterInfo,
                   final HttpServletRequest request,
                   final HttpServletResponse response);
}
