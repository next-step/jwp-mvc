package core.mvc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ViewResolverTest {

    @DisplayName("viewName 앞에 redirect: 접두사가 붙으면 RedirectView, 아니라면 ForwardView 로 처리해야 한다.")
    @Test
    void viewResolveTest() {
        String redirectViewName = "redirect:/";
        String forwardViewName = "/users/create";
        ViewResolver viewResolver = new ViewResolver();

        View resolveView = viewResolver.resolveViewName(redirectViewName);
        View forwardView = viewResolver.resolveViewName(forwardViewName);

        Assertions.assertThat(resolveView.getClass()).isEqualTo(RedirectView.class);
        Assertions.assertThat(forwardView.getClass()).isEqualTo(ForwardView.class);
    }

}
