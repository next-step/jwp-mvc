package core.mvc.param.parser.annotation;

import core.mvc.param.Parameter;
import core.mvc.param.parser.ValueExtractor;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationValueExtractors implements ValueExtractor {
    private static final Map<Class<? extends Annotation>, AnnotationValueExtractor<? extends Annotation>> EXTRACTORS =
            Stream.of(new PathVariableExtractor(), new RequestParamValueExtractor())
                    .collect(Collectors.toMap(AnnotationValueExtractor::getAnnotation, Function.identity()));

    @Override
    public Object extract(Parameter parameter, HttpServletRequest request) {
        if (EXTRACTORS.containsKey(parameter.getAnnotation())) {
            return null;
        }

        return EXTRACTORS.get(parameter.getAnnotation())
                .extract(parameter, request);
    }
}
