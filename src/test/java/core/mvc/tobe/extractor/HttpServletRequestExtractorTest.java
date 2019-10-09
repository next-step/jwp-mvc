package core.mvc.tobe.extractor;

import core.mvc.tobe.ParameterInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class HttpServletRequestExtractorTest extends ExtractorTestSupport {

    @Override
    RequestParameterExtractor getExtractor() {
        return new HttpServletRequestExtractor();
    }

    @DisplayName("HttpServletRequest 매칭 시 true를 반환한다.")
    @Test
    void isSupportTrue() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class,
                "targetMethod", HttpServletRequest.class);
        assertThat(isSupport(parameterInfo)).isTrue();

    }

    @DisplayName("HttpServletRequest 이 아닌 값 매칭 시 false를 반환한다.")
    @Test
    void isSupportNonTarget() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class,
                "nonTargetMethod", String.class);
        assertThat(isSupport(parameterInfo)).isFalse();
    }

    @DisplayName("HttpServletRequest 추출 시 입력한 Request를 반환한다.")
    @Test
    void extract() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class,
                "targetMethod", HttpServletRequest.class);

        final Object extracted = extract(parameterInfo);

        assertThat(extracted).isInstanceOf(HttpServletRequest.class);
    }

    @DisplayName("HttpServletRequest 가 아닌 값 추출 시 null을 반환한다.")
    @Test
    void extractNotFound() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class,
                "nonTargetMethod", String.class);

        final Object extracted = extract(parameterInfo);

        assertThat(extracted).isNull();
    }

    static class Mock {
        public void targetMethod(HttpServletRequest value) {
        }

        public void nonTargetMethod(String value) {
        }
    }
}