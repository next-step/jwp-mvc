package core.mvc.param.extractor.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

@DisplayName("@RequestParam 이 붙은 파라미터 값을 추출하는 클래스")
class RequestParamValueExtractorTest {

    @Test
    @DisplayName("쿼리 스트링으로 부터 값을 제대로 가지고 오는지 확인")
    void test() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test?id=1");

        System.out.println(request.getParameter("id"));
    }

}