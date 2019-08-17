package core.mvc.tobe.view;

import core.mvc.View;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static core.mvc.tobe.view.ViewGenerator.DEFAULT_JSP_PREFIX;
import static core.mvc.tobe.view.ViewGenerator.DEFAULT_REDIRECT_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;

class ViewGeneratorTest {

    @DisplayName("Jsp view 를 생성한다")
    @Test
    void of_JspView() {
        String viewName = "/users/list" + DEFAULT_JSP_PREFIX;

        View view = ViewGenerator.of(viewName);

        assertThat(view).isInstanceOf(JspView.class);
    }

    @DisplayName("Redirect view 를 생성한다")
    @Test
    void of_RedirectView() {
        String viewName = DEFAULT_REDIRECT_PREFIX + "/users";

        View view = ViewGenerator.of(viewName);

        assertThat(view).isInstanceOf(RedirectView.class);
    }

    @DisplayName("지원하지 않는 경우 null을 반환한다")
    @Test
    void of_whenInputWrong_thenReturnNull() {
        String viewName = "user.html";

        View view = ViewGenerator.of(viewName);

        assertThat(view).isNull();
    }
}