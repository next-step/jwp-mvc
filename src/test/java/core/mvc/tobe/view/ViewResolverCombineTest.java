package core.mvc.tobe.view;

import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewResolverCombineTest {

    @Test
    void viewResolverManger() throws ServletException {
        ViewResolverManager viewResolverManager = new ViewResolverManager();

        final View redirectView = viewResolverManager.resolveView("redirect:redirect-result.jsp");
        final View forwardView = viewResolverManager.resolveView("forward:forward-result.jsp");
        final View defaultForwardView = viewResolverManager.resolveView("forward-result.jsp");

        assertThat(redirectView).isExactlyInstanceOf(RedirectView.class);
        assertThat(forwardView).isExactlyInstanceOf(ForwardView.class);
        assertThat(defaultForwardView).isExactlyInstanceOf(ForwardView.class);
    }

}
