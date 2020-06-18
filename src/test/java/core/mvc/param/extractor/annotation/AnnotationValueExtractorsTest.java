package core.mvc.param.extractor.annotation;

import core.AnnotationInstance;
import core.annotation.web.PathVariable;
import core.mvc.param.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("여러 어노테이션 값 추출기가 들어있는 객체 테스트")
class AnnotationValueExtractorsTest {
    private static final AnnotationValueExtractors ANNOTATION_VALUE_EXTRACTORS = new AnnotationValueExtractors();

    @Test
    @DisplayName("해당하는 어노테이션에 대한 추출기가 있는지 테스트")
    void extract() {
        Parameter parameter = new Parameter(
                "userId",
                int.class,
                AnnotationInstance.newPathVariable("userId", "userId", true)
        );
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("userId", "1");

        Object extract = ANNOTATION_VALUE_EXTRACTORS.extract(parameter, request);
        assertThat(extract).isEqualTo(1);
    }

    @Test
    @DisplayName("해당하는 어노테이션에 대한 추출기가 없다면 null을 반환")
    void extractFail() {
        Parameter parameter = new Parameter("userId", int.class, () -> null);
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThat(ANNOTATION_VALUE_EXTRACTORS.extract(parameter, request)).isNull();
    }

}