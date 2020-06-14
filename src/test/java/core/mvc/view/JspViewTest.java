package core.mvc.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Jsp 뷰")
class JspViewTest {

    @Test
    @DisplayName("초기화 테스트")
    void constructor() {
        assertThatCode(() -> new JspView("test.jsp"))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("초기화 실패 테스트 .jsp 로 끝나지 않는다면 예외 발생")
    void constructorThrowException(final String viewName) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new JspView(viewName));
    }

    private static Stream<String> constructorThrowException() {
        return Stream.of(
                null,
                "",
                "some.html"
        );
    }

    @Test
    @DisplayName("렌더링시 모델에 있는 값들은 request 의 attribute 로 전달")
    void render() throws Exception {
        JspView jspView = new JspView("test.jsp");
        Map<String, Object> model = new HashMap<>();
        model.put("test", "test");
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();


        assertThat(request.getAttribute("test")).isNull();
        jspView.render(model, request, response);
        assertThat(request.getAttribute("test")).isNotNull();
    }
}
