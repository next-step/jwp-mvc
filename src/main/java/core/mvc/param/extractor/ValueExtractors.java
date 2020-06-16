package core.mvc.param.extractor;

import core.exception.ParameterNotFoundException;
import core.mvc.param.Parameter;
import core.mvc.param.extractor.annotation.AnnotationValueExtractors;
import core.mvc.param.extractor.complex.ComplexValueExtractor;
import core.mvc.param.extractor.simple.SimpleValueExtractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ValueExtractors {
    private static final List<ValueExtractor> EXTRACTORS = Arrays.asList(
            new SimpleValueExtractor(),
            new ComplexValueExtractor(),
            new AnnotationValueExtractors()
    );

    public static Object extractValue(final Parameter parameter, final HttpServletRequest request) {
        return EXTRACTORS.stream()
                .map(extractor -> extractor.extract(parameter, request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new ParameterNotFoundException(parameter.getName()));
    }
}
