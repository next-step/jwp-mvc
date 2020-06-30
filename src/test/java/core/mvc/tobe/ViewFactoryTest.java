package core.mvc.tobe;

import core.mvc.JspView;
import core.mvc.RedirectView;
import core.mvc.View;
import core.mvc.ViewFactory;
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
