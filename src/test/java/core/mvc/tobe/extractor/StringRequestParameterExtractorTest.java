package core.mvc.tobe.extractor;

import core.mvc.tobe.ParameterInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class StringRequestParameterExtractorTest extends ExtractorTestSupport {

    private static final String VALUE = "VALUE";

    @Override
    RequestParameterExtractor getExtractor() {
        return new StringRequestParameterExtractor();
    }

    @Override
    HttpServletRequest getRequest() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("value", VALUE);

        return request;
    }

    @DisplayName("String 매칭 시 true를 반환한다.")
    @Test
    void isSupportTrue() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethod", String.class);
        assertThat(isSupport(parameterInfo)).isTrue();
    }

    @DisplayName("String이 아닌 값 매칭 시 false를 반환한다.")
    @Test
    void isSupportNonTarget() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "nonTargetMethod", int.class);
        assertThat(isSupport(parameterInfo)).isFalse();
    }

    @DisplayName("String 추출 시 값을 반환한다.")
    @Test
    void extract() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethod", String.class);

        final Object extracted = extract(parameterInfo);

        assertThat(extracted).isEqualTo(VALUE);
    }

    @DisplayName("String 이 아닌 값 추출 시 null을 반환한다.")
    @Test
    void extractNotFound() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "nonTargetMethod", int.class);

        final Object extracted = extract(parameterInfo);

        assertThat(extracted).isNull();
    }

    static class Mock {

        public void targetMethod(String value) {
        }
        public void nonTargetMethod(int value) {
        }
    }
}