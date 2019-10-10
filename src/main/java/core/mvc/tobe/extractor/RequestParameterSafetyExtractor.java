package core.mvc.tobe.extractor;

import core.mvc.tobe.ParameterInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract class RequestParameterSafetyExtractor implements RequestParameterExtractor {

    abstract Object safetyExtract(final ParameterInfo parameterInfo,
                                  final HttpServletRequest request,
                                  final HttpServletResponse response);

    @Override
    public final Object extract(final ParameterInfo parameterInfo,
                                final HttpServletRequest request,
                                final HttpServletResponse response) {
        return isSupport(parameterInfo) ? safetyExtract(parameterInfo, request, response) : null;
    }
}
