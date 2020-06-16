package core.mvc.param.parser.annotation;

import core.annotation.web.PathVariable;
import core.mvc.param.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("@PathVariable 어노테이션이 붙은 변수를 url로 부터 잘 가지고 오는지 테스트")
class PathVariableExtractorTest {
    private static final PathVariableExtractor PATH_VARIABLE_EXTRACTOR = new PathVariableExtractor();

    @Test
    @DisplayName("추출을 잘 하는지 테스트")
    void extract() {
        Parameter parameter = new Parameter("userId", int.class, PathVariable.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("userId", 1);

        Object value = PATH_VARIABLE_EXTRACTOR.extract(parameter, request);

        assertThat(value).isEqualTo(1);
    }

}