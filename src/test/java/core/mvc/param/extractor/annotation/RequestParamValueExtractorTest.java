package core.mvc.param.extractor.annotation;

import core.exception.ParameterNotFoundException;
import core.mvc.param.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static core.AnnotationInstance.newRequestParam;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("@RequestParam 이 붙은 파라미터 값을 추출하는 클래스")
class RequestParamValueExtractorTest {
    private final RequestParamValueExtractor EXTRACTOR = new RequestParamValueExtractor();

    @Test
    @DisplayName("파라미터(쿼리 스트링과 폼 데이터는 파라미터에 자동으로 파싱되어 저장된다를 전제)로 부터 값을 제대로 가지고 오는지 확인")
    void getParam() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        request.setParameter("id", "1");

        Object value = EXTRACTOR.extract(
                new Parameter(
                        "id",
                        String.class,
                        newRequestParam("id", "id", true)
                ),
                request
        );

        assertThat(value).isInstanceOf(String.class);
        assertThat(value).isEqualTo("1");
    }

    @Test
    @DisplayName("파라미터가 존재하지 않고 값이 필수가 아닌 상황에서는 null 을 반환한다")
    void getParamRequiredFalse() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");

        Object value = EXTRACTOR.extract(
                new Parameter(
                        "id",
                        String.class,
                        newRequestParam("id", "id", false)
                ),
                request
        );

        assertThat(value).isNull();
    }

    @Test
    @DisplayName("파라미터가 존재하지 않고 값이 필수인 상황에서는 예외를 반환한다")
    void getParamRequiredTrue() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");

        assertThatExceptionOfType(ParameterNotFoundException.class)
                .isThrownBy(() -> EXTRACTOR.extract(
                        new Parameter(
                                "id",
                                String.class,
                                newRequestParam("id", "id", true)
                        ),
                        request
                ));
    }

    @Test
    @DisplayName("request parameter 의 value 가 존재하지 않으면 parameter name 으로 값을 가져온다")
    void getParamWithoutValue() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        request.addParameter("id", "1");

        Object value = EXTRACTOR.extract(
                new Parameter(
                        "id",
                        String.class,
                        newRequestParam("", "id", false)
                ),
                request
        );

        assertThat(value).isInstanceOf(String.class);
        assertThat(value).isEqualTo("1");
    }
}