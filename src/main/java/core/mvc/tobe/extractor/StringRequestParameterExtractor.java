package core.mvc.tobe.extractor;

import core.mvc.tobe.ParameterInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StringRequestParameterExtractor extends RequestParameterSafetyExtractor {

    @Override
    public boolean isSupport(final ParameterInfo parameterInfo) {
        return parameterInfo.matchType(String.class);
    }

    @Override
    public String safetyExtract(final ParameterInfo parameterInfo,
                                final HttpServletRequest request,
                                final HttpServletResponse response) {
        return request.getParameter(parameterInfo.getParameterName());
    }
}
