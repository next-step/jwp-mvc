package core.mvc.tobe.extractor;

import core.annotation.web.PathVariable;
import core.mvc.tobe.ParameterInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class PathVariableExtractorTest extends ExtractorTestSupport {

    private static final int INT_ID = 100;
    private static final long LONG_ID = 100L;

    @Override
    RequestParameterExtractor getExtractor() {
        return new PathVariableExtractor();
    }

    @Override
    HttpServletRequest getRequest() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/" + LONG_ID);

        return request;
    }

    @DisplayName("PathVariable int 매칭 시 true를 반환한다.")
    @Test
    void isSupportIntTrue() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethodInt", int.class);
        assertThat(isSupport(parameterInfo)).isTrue();
    }

    @DisplayName("PathVariable long 매칭 시 true를 반환한다.")
    @Test
    void isSupportLongTrue() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethodLong", long.class);
        assertThat(isSupport(parameterInfo)).isTrue();
    }

    @DisplayName("PathVariable가 아닌 값 매칭 시 false를 반환한다.")
    @Test
    void isSupportNonTarget() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "nonTargetMethod", int.class);
        assertThat(isSupport(parameterInfo)).isFalse();
    }

    @DisplayName("PathVariable int 추출 시 값을 반환한다.")
    @Test
    void extractInt() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethodInt", int.class);

        final Object extracted = extract(parameterInfo);

        assertThat(extracted).isEqualTo(INT_ID);
    }

    @DisplayName("PathVariable long 추출 시 값을 반환한다.")
    @Test
    void extractLong() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethodLong", long.class);

        final Object extracted = extract(parameterInfo);

        assertThat(extracted).isEqualTo(LONG_ID);
    }

    @DisplayName("PathVariable name 기준으로 추출 시 값을 반환한다.")
    @Test
    void extractByName() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethodName", int.class);

        final Object extracted = extract(parameterInfo);

        assertThat(extracted).isEqualTo(INT_ID);
    }

    @DisplayName("PathVariable value 기준으로 추출 시 값을 반환한다.")
    @Test
    void extractByValue() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethodValue", int.class);

        final Object extracted = extract(parameterInfo);

        assertThat(extracted).isEqualTo(INT_ID);
    }

    @DisplayName("PathVariable 가 아닌 값 추출 시 null을 반환한다.")
    @Test
    void extractNotFound() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "nonTargetMethod", int.class);

        final Object extracted = extract(parameterInfo);

        assertThat(extracted).isNull();
    }

    @DisplayName("PathVariable value로 매칭 시 true를 반환한다.")
    @Test
    void isSupportTrue() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethodInt", int.class);
        assertThat(isSupport(parameterInfo)).isTrue();
    }

    public static class Mock {

        public void targetMethodInt(@PathVariable int value) {
        }
        public void targetMethodLong(@PathVariable long value) {
        }
        public void targetMethodValue(@PathVariable(value = "value") int ignore) {
        }
        public void targetMethodName(@PathVariable(name = "value") int ignore) {
        }
        public void nonTargetMethod(int value) {
        }
    }
}