package core.mvc.tobe.extractor;

import core.mvc.tobe.ParameterInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class LongRequestParameterExtractorTest extends ExtractorTestSupport {

    private static final long VALUE = 100L;

    @Override
    RequestParameterExtractor getExtractor() {
        return new LongRequestParameterExtractor();
    }

    @Override
    HttpServletRequest getRequest() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("value", String.valueOf(VALUE));

        return request;
    }

    @DisplayName("long 매칭 시 true를 반환한다.")
    @Test
    void isSupportTrue() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethod", long.class);
        assertThat(isSupport(parameterInfo)).isTrue();
    }

    @DisplayName("long 이 아닌 값 매칭 시 false를 반환한다.")
    @Test
    void isSupportNonTarget() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "nonTargetMethod", String.class);
        assertThat(isSupport(parameterInfo)).isFalse();
    }

    @DisplayName("long 추출 시 값을 반환한다.")
    @Test
    void extract() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethod", long.class);

        final Object extracted = extract(parameterInfo);

        assertThat(extracted).isEqualTo(VALUE);
    }

    @DisplayName("long 가 아닌 값 추출 시 null을 반환한다.")
    @Test
    void extractNotFound() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "nonTargetMethod", String.class);

        final Object extracted = extract(parameterInfo);

        assertThat(extracted).isNull();
    }

    static class Mock {
        public void targetMethod(long value) {
        }

        public void nonTargetMethod(String value) {
        }
    }
}