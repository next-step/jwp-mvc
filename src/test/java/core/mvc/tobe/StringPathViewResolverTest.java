package core.mvc.tobe;

import core.mvc.tobe.resolver.StringPathViewResolver;
import core.mvc.tobe.resolver.ViewResolver;
import core.mvc.tobe.view.JspView;
import core.mvc.tobe.view.RedirectView;
import core.mvc.tobe.view.View;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringPathViewResolverTest {

    @Test
    public void jspView() {
        String pathName = "/users";

        ViewResolver viewResolver = new StringPathViewResolver();
        View view = viewResolver.resolveViewName(pathName);

        assertThat(view).isEqualToComparingFieldByField(new JspView(pathName));
    }

    @Test
    public void redirectView() {
        String pathName = "redirect:/users";

        ViewResolver viewResolver = new StringPathViewResolver();

        View view = viewResolver.resolveViewName(pathName);

        assertThat(view).isEqualToComparingFieldByField(new RedirectView(pathName.substring("redirect:".length())));
    }
}
