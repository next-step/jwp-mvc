package core.mvc.tobe.extractor;

import core.annotation.web.PathVariable;
import core.mvc.tobe.ParameterInfo;
import core.mvc.tobe.utils.PathUtils;
import core.mvc.tobe.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PathVariableExtractor extends RequestParameterSafetyExtractor {

    @Override
    public boolean isSupport(final ParameterInfo parameterInfo) {
        return parameterInfo.getAnnotation(PathVariable.class).isPresent();
    }

    @Override
    Object safetyExtract(final ParameterInfo parameterInfo,
                         final HttpServletRequest request,
                         final HttpServletResponse response) {
        return PathUtils.parse(parameterInfo.getPath(), request.getRequestURI(), getPathVariableName(parameterInfo))
                .map(value -> PrimitiveConverter.convert(parameterInfo.getType(), value))
                .orElse(null);

    }

    private String getPathVariableName(final ParameterInfo parameterInfo) {
        final PathVariable pathVariable = parameterInfo.getAnnotation(PathVariable.class)
                .orElseThrow();

        if (StringUtils.isNotBlank(pathVariable.name())) {
            return pathVariable.name();
        }
        if (StringUtils.isNotBlank(pathVariable.value())) {
            return pathVariable.value();
        }

        return parameterInfo.getParameterName();
    }
}
