package core.mvc.tobe.extractor;

import core.mvc.tobe.ParameterInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class PrimitiveExtractor extends RequestParameterSafetyExtractor {

    abstract Class<?> getType();

    @Override
    public boolean isSupport(final ParameterInfo parameterInfo) {
        return parameterInfo.isPrimitive();
    }

    @Override
    Object safetyExtract(final ParameterInfo parameterInfo,
                         final HttpServletRequest request,
                         final HttpServletResponse response) {
        return PrimitiveConverter.convert(getType(), request.getParameter(parameterInfo.getParameterName()));
    }
}
