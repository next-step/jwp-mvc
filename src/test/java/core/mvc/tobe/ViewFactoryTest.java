package core.mvc.tobe;

import core.mvc.tobe.view.JspView;
import core.mvc.tobe.view.RedirectView;
import core.mvc.tobe.view.View;
import core.mvc.tobe.view.ViewFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewFactoryTest {

    @Test
    public void createJspView() {
        String pathName = "/users";

        View view = ViewFactory.create(pathName);

        assertThat(view).isEqualToComparingFieldByField(new JspView(pathName));
    }

    @Test
    public void createRedirectView() {
        String pathName = "redirect:/users";

        View view = ViewFactory.create(pathName);

        assertThat(view).isEqualToComparingFieldByField(new RedirectView(pathName));
    }
}
