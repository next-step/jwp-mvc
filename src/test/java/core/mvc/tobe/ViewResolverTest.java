package core.mvc.tobe;

import core.mvc.tobe.resolver.view.ViewResolveParameter;
import core.mvc.tobe.resolver.view.ViewResolver;
import core.mvc.tobe.resolver.view.ViewResolvers;
import core.mvc.tobe.view.JspView;
import core.mvc.tobe.view.RedirectView;
import core.mvc.tobe.view.View;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewResolverTest {

    @Test
    public void jspView() {
        String pathName = "/user.jsp";

        ViewResolveParameter parameter = new ViewResolveParameter(pathName);
        ViewResolver viewResolver = ViewResolvers.getResolver(parameter);
        View view = viewResolver.resolve(parameter);

        assertThat(view).isEqualToComparingFieldByField(new JspView(pathName));
    }

    @Test
    public void redirectView() {
        String pathName = "redirect:/user.jsp";

        ViewResolveParameter parameter = new ViewResolveParameter(pathName);
        ViewResolver viewResolver = ViewResolvers.getResolver(parameter);
        View view = viewResolver.resolve(parameter);

        assertThat(view).isEqualToComparingFieldByField(new RedirectView(pathName.replace("redirect:", "")));
    }

}
